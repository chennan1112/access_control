package com.unicom.access.service;

import com.unicom.access.entity.TimeTask;
import com.unicom.access.entity.ViewParam;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/3 10:47
 */
public interface TimeTaskService {
    /**
     * 插入
     *
     * @param timeTask
     * @return
     */
    Integer insert(TimeTask timeTask);


    /**
     * 根据id删除
     *
     * @param timeTask
     */
    Integer updateById(TimeTask timeTask);

    /**
     * 查询数据
     *
     * @return
     */
    List<TimeTask> selectAll(TimeTask timeTask);
}
