/*
 * Copyright (c) 2005-2018 , FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package com.chris.poi.generalsimplepoi.poi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ExcelUtil {
    public static final String Format_DateTime = "yyyy-MM-dd HH:mm:ss";
    public static final int EXCEL_BEGIN_ROW_INDEX = 2;
    private SXSSFSheet sheet;
    private String[] headers;
    private String[] properties;
    private CellStyle titleStyle;
    private CellStyle headerStyle;
    private CellStyle cellStyle;
    private final String title;
    private List<ExcelField> headList;
    private AtomicInteger sheetCount = new AtomicInteger(0);

    /**
     * 通用EXCEL导出
     * 调用前请先调用 initExport(workbook, headList);
     *
     * @param workbook
     * @param centnts  内容
     * @param startRow
     */
    public void exportXLSX(SXSSFWorkbook workbook, List<Map<String, Object>> centnts, int startRow) {

        // 遍历集合数据，产生数据行
        int rowIndex = startRow;

        for (Map<String, Object> obj : centnts) {
            //首or尾行
            synchronized (sheet) {
                if (rowIndex == 0) {
                    //数据内容从 rowIndex=2开始
                    rowIndex += EXCEL_BEGIN_ROW_INDEX;
                } else if ((rowIndex %= 10) == 0) {
                    //如果数据超过了，则在第二页显示
                    sheetCount.addAndGet(1);
                    rowIndex += EXCEL_BEGIN_ROW_INDEX;
                    initHeaderAndSheet(workbook, headList, title);
                }
            }

            SXSSFRow dataRow;
            dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                SXSSFCell newCell = dataRow.createCell(i);

                Object o = obj.get(properties[i]);
                String cellValue = "";
                if (o == null) {
                    cellValue = "";
                } else if (o instanceof Date) {
                    cellValue = formatDate((Date) o, Format_DateTime);
                }
                //不对浮点数处理
      /*          else if (o instanceof Float || o instanceof Double)
                    cellValue = new BigDecimal(o.toString()).setScale(4, BigDecimal.ROUND_HALF_UP).toString();*/
                else {
                    cellValue = o.toString();
                }

                newCell.setCellValue(cellValue);
                newCell.setCellStyle(cellStyle);
            }
            rowIndex++;
        }

    }


    /**
     * 设置excel样式，生成第一行Header数据
     *
     * @param workbook
     * @param title    头
     * @param headList 列头 用map无法保证列的顺序，所以改用list
     */
    public ExcelUtil(SXSSFWorkbook workbook, List<ExcelField> headList, String title) {
        this.title = title;
        this.headList = headList;

        workbook.setCompressTempFiles(true);
        //表头样式
        // 头单元格样式
        titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font fontStyle = workbook.createFont(); // 字体样式
        fontStyle.setBold(true); // 加粗
        fontStyle.setFontName("宋体"); // 字体
        fontStyle.setFontHeightInPoints((short) 18); // 大小
        titleStyle.setFont(fontStyle);
        // 列头样式
        headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setFontName("宋体");
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        // 单元格样式
        cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        cellFont.setFontHeightInPoints((short) 10);
        cellFont.setFontName("宋体");
        cellStyle.setFont(cellFont);

        initHeaderAndSheet(workbook, headList, title);
    }

    private void initHeaderAndSheet(SXSSFWorkbook workbook, List<ExcelField> headList, String title) {

        // 生成一个(带标题)表格
        sheet = workbook.createSheet("表头" + sheetCount);
        //设置列宽
        int minBytes = 17;
        int headerSize = headList.size();
        int[] arrColWidth = new int[headerSize];
        // 产生表格标题行,以及设置列宽
        properties = new String[headerSize];
        headers = new String[headerSize];
        int ii = 0;
        for (ExcelField field : headList) {
            String fieldName = field.getName();
            properties[ii] = fieldName;
            headers[ii] = field.getValue();
            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }

        SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
        titleRow.createCell(0).setCellValue(title);
        titleRow.getCell(0).setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerSize - 1));

        SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);

        }
    }

    /**
     * 读取 office 2003 excel 返回你一个你想要的对象列表(仅支持单个sheet)
     *
     * @param file
     * @param beginRowNum 开始读取的行数
     * @author caizq
     * @ 支持字符型与日期型单元格
     * @date 2017/10/27
     * @since v1.0.0
     */
    public static <T> List<T> read2003Excel(MultipartFile file, int beginRowNum, Class clazz)
            throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        HSSFWorkbook hwb = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = hwb.getSheetAt(0);
        T value = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        int counter = 0;
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();
        List<T> list = new LinkedList<T>();
        for (int i = sheet.getFirstRowNum() + beginRowNum; counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                break;
            } else {
                counter++;
            }
            T obj = (T) clazz.newInstance();
            for (int j = row.getFirstCellNum(); j < fields.length; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                // 格式化 number String
                DecimalFormat df = new DecimalFormat("0");
                // 字符 格式化数字
                DecimalFormat nf = new DecimalFormat("0.00");
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:
                        setObjProperties(methods, fields[j], obj, cell.getStringCellValue().trim());
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            setObjProperties(methods, fields[j], obj, df.format(cell.getNumericCellValue()));
                        } else if ("General".equals(cell.getCellStyle()
                                .getDataFormatString())) {
                            setObjProperties(methods, fields[j], obj, nf.format(cell.getNumericCellValue()));
                        } else {
                            setObjProperties(methods, fields[j], obj, HSSFDateUtil.getJavaDate(cell
                                    .getNumericCellValue()));
                        }
                        break;
                    //				case XSSFCell.CELL_TYPE_BOOLEAN:
                    //					value = cell.getBooleanCellValue();
                    //					break;
                    //				case XSSFCell.CELL_TYPE_BLANK:
                    //					value = "";
                    //					break;
                    default:
                        setObjProperties(methods, fields[j], obj, cell.toString().trim());
                }
            }
            if (checkObjFieldIsNotNull(obj)) {
                //整行不为空
                list.add(obj);
            }
        }
        return list;
    }

    private static boolean checkObjFieldIsNotNull(Object obj) {
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) != null) {
                    //是字符串类型
                    if (f.getType() == String.class && !StringUtils.isEmpty((String) f.get(obj))) {
                        return true;
                    }
                    //非字符串类型
                    else if (!(f.getType() == String.class)) {
                        return true;
                    }
                }

            }
        } catch (IllegalAccessException e) {
            //log.error("checkObjFieldIsNotNull error excel转换失败");
        }
        return false;
    }

    private static void setObjProperties(Method[] methods, Field field, Object obj, Object value)
            throws InvocationTargetException, IllegalAccessException {
        boolean done = false;
        for (int k = 0; k < methods.length; k++) {
            if (methods[k].getName().indexOf("set") == 0) {
                String setMethodName = methods[k].getName().substring(3, methods[k].getName().length());
                if (setMethodName.equalsIgnoreCase(field.getName())) {
                    //调用set方法
                    methods[k].invoke(obj, value);
                    done = true;
                    break;
                }
            }
            if (done) {
                done = true;
                break;
            }
        }
    }

    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
        // 第一步，创建一个webbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setColumnWidth(0, 2500);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(3, 7000);
        sheet.setColumnWidth(4, 4000);
        sheet.setDefaultRowHeightInPoints(20);

        //头部
        HSSFCellStyle titleStyle = getHeadCellStyle(wb);

        HSSFCellStyle dateStyle = getDateCellStyle(wb);
        //内容
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);  //字体大小
        HSSFDataFormat format = wb.createDataFormat();
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
        style.setDataFormat(format.getFormat("@"));
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style.setFont(font);
        HSSFCell cell = null;
        //  fontStyle.setFontHeight((short)24);

        for (int i = 0; i < 3; i++) {
            HSSFRow row = sheet.createRow(i);
            for (int j = 0; j < title.length; j++) {
                Cell rowCell = row.createCell(j);
                if (i == 0 && j == 0) {
                    rowCell.setCellValue(sheetName);
                    rowCell.setCellStyle(titleStyle);
                }
                if (i == 2 && j == 0) {
                    rowCell.setCellStyle(dateStyle);
                    rowCell.setCellValue("打印时间：" + getCurrentDate(Format_DateTime));
                }
            }
        }

        // 合并单元格
        CellRangeAddress cra = new CellRangeAddress(0, 1, 0, title.length - 1); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra);
        CellRangeAddress cra1 = new CellRangeAddress(2, 2, 0, title.length - 1); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra1);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(3);
        // 第四步，创建单元格，并设置值表头 设置表头居中

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 3, k = 0; k < values.length; i++, k++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[k].length; j++) {
                Cell rowCell = row.createCell(j);
                rowCell.setCellStyle(style);
                rowCell.setCellValue(values[k][j]);
            }
        }

        return wb;
    }

    private static HSSFCellStyle getHeadCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle titleStyle = wb.createCellStyle(); // 头单元格样式
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont fontStyle = wb.createFont(); // 字体样式
        fontStyle.setBold(true); // 加粗
        fontStyle.setFontName("宋体"); // 字体
        fontStyle.setFontHeightInPoints((short) 24); // 大小
        titleStyle.setFont(fontStyle);
        return titleStyle;
    }

    private static HSSFCellStyle getDateCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle dateStyle = wb.createCellStyle(); // 日期单元格样式
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        HSSFFont dateFont = wb.createFont(); // 字体样式
        dateFont.setFontName("宋体"); // 字体
        dateFont.setFontHeightInPoints((short) 11); // 大小
        dateStyle.setFont(dateFont);
        return dateStyle;
    }

    public static String formatDate(Date date, String format) {
        String s = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            s = sdf.format(date);
        }

        return s;
    }

    /**
     * 获取指定格式的当前系统日期
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat t = new SimpleDateFormat(format);
        return t.format(new Date());
    }
}
