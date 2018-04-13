package com.chris.poi.generalsimplepoi.poi.mapper;

import com.chris.poi.generalsimplepoi.poi.domain.Snatched;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SnatchedMapper {
    @Select("SELECT * FROM snatched limit #{start},#{end}")
    List<Map<String,Object>> getAllByLimited(@Param("start")int start,@Param("end") int end);
    @Select("SELECT count(1) FROM snatched")
    Integer getCount();
}
