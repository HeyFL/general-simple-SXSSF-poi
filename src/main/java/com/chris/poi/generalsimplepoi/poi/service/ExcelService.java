/*
 * Copyright (c) 2005-2018 , FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package com.chris.poi.generalsimplepoi.poi.service;

import com.chris.poi.generalsimplepoi.poi.mapper.SnatchedMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public class ExcelService implements IExcelService{
    private final SnatchedMapper snatchedMapper;

    public ExcelService(SnatchedMapper snatchedMapper) {
        this.snatchedMapper = snatchedMapper;
    }

    @Override
    public List<Map<String, Object>> getAllByLimited(int start,int end){
        long a = System.currentTimeMillis();
        List<Map<String,Object>> result = snatchedMapper.getAllByLimited(start, end);
        log.debug("查询耗时:{}",String.valueOf(System.currentTimeMillis()-a));
        return result;
    }
    @Override
    public Integer getCount(){
        return snatchedMapper.getCount();
    }

}
