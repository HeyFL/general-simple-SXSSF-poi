/*
 * Copyright (c) 2005-2018 , FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package com.chris.poi.generalsimplepoi.poi.web;

import com.chris.poi.generalsimplepoi.poi.service.IExcelService;
import com.chris.poi.generalsimplepoi.poi.util.DateUtil;
import com.chris.poi.generalsimplepoi.poi.util.ExcelField;
import com.chris.poi.generalsimplepoi.poi.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/mytest")
public class WebControllor {
    private static ExecutorService pool = Executors.newFixedThreadPool(5);

    //每次往Excel写几条数据
    public static final int ROW_ACCESS_WINDOW_SIZE = 5000;
    @Autowired
    IExcelService excelService;

    @ResponseBody
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public ResponseEntity execute() throws UnsupportedEncodingException, InterruptedException {
        String title="测试";
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //1.获取总条数
        int count= excelService.getCount();

        //2.计算查询次数 TODO 算的不对
        int list_count = count/ROW_ACCESS_WINDOW_SIZE;
        list_count = count%ROW_ACCESS_WINDOW_SIZE>0?list_count+1:list_count;

        //3.按次数将数据写入文件
        HttpHeaders headers = getDownloadHeader();
        List<ExcelField> excelFields = getExcelFields();

        System.out.println("开始导出");
        long start = System.currentTimeMillis();
        SXSSFWorkbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        ExcelUtil excelUtil = new ExcelUtil(workbook,excelFields,title);
        list_count=20;
        System.out.println("任务数:"+list_count);
        for (int i = 0; i < list_count; i++) {
            final int finalI = i;
            //pool.submit(()->{
                int startNum  = finalI *ROW_ACCESS_WINDOW_SIZE;
                List<Map<String, Object>> result = excelService.getAllByLimited(startNum,ROW_ACCESS_WINDOW_SIZE);
                excelUtil.exportXLSX(workbook,result,startNum);
            //});
        }
        //pool.shutdown();
        //while (!pool.isTerminated()) {
        //    Thread.sleep(100);
        //}

        workbook.sheetIterator().next();
        System.out.println("完成任务:"+String.valueOf(System.currentTimeMillis()-start));
        try {
            workbook.write(out);
            workbook.close();
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
    }

    private List<ExcelField> getExcelFields() {
        List<ExcelField> excelFields = new ArrayList<>(10);
        excelFields.add(new ExcelField("id", "id"));
        excelFields.add(new ExcelField("torrentid", "torrentid"));
        excelFields.add(new ExcelField("userid", "userid"));
        excelFields.add(new ExcelField("ip", "ip"));
        excelFields.add(new ExcelField("port", "port"));
        excelFields.add(new ExcelField("uploaded", "uploaded"));
        excelFields.add(new ExcelField("downloaded", "downloaded"));
        excelFields.add(new ExcelField("to_go", "to_go"));
        excelFields.add(new ExcelField("seedtime", "seedtime"));
        excelFields.add(new ExcelField("leechtime", "leechtime"));
        excelFields.add(new ExcelField("last_action", "last_action"));
        excelFields.add(new ExcelField("startdat", "startdat"));
        excelFields.add(new ExcelField("completedat", "completedat"));
        excelFields.add(new ExcelField("finished", "finished"));
        return excelFields;
    }

    /**
     * 获得返回前端header 如 默认下载文件名等
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpHeaders getDownloadHeader() throws UnsupportedEncodingException {
        //下载显示的文件名，解决中文名称乱码问题
        String fileName = "Pallet Entruck-" + DateUtil.getCurrentDate(DateUtil.Format_DateTime) + ".xls";
        String downloadFielName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        //通知浏览器以attachment（下载方式）
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }

}
