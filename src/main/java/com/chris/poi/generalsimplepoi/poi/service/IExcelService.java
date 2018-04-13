package com.chris.poi.generalsimplepoi.poi.service;

import java.util.List;
import java.util.Map;

public interface IExcelService{
        public List<Map<String, Object>> getAllByLimited(int start,int end);

        Integer getCount();
}
