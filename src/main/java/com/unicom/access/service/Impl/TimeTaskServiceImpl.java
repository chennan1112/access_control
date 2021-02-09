package com.unicom.access.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.unicom.access.entity.TimeTask;
import com.unicom.access.entity.ViewParam;
import com.unicom.access.mapper.TimeTaskMapper;
import com.unicom.access.service.TimeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/3 10:48
 */
@Component
public class TimeTaskServiceImpl implements TimeTaskService {

    @Autowired
    TimeTaskMapper timeTaskMapper;

    @Override
    public Integer insert(TimeTask timeTask) {
        try {
            Integer it = timeTaskMapper.insert(timeTask);
            return it;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public Integer updateById(TimeTask timeTask) {
        timeTask.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        Integer it = timeTaskMapper.updateById(timeTask);
        return it;
    }

    @Override
    public List<TimeTask> selectAll(TimeTask timeTask) {
        EntityWrapper<TimeTask>  en=new EntityWrapper<>();
        en.eq("account_id",timeTask.getAccountId());
        return timeTaskMapper.selectList(en);
    }
}
