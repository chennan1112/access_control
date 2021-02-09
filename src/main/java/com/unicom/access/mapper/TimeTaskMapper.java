package com.unicom.access.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.unicom.access.entity.TimeTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/3 10:46
 */
@Mapper
public interface TimeTaskMapper extends BaseMapper<TimeTask> {

    @Select("<script>"
            + "select * from time_task limit #{start},#{num}; "
            + "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "weekDay", column = "week_day"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "isDelete", column = "is_delete"),
    })
    List<TimeTask> selectByLimit(@Param("start") Long start, @Param("num") Long num);
}
