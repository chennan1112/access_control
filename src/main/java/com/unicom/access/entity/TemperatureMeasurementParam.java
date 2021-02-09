package com.unicom.access.entity;

import lombok.Data;

/**
 * @author mrChen
 * @date 2021/1/29 19:45
 */
@Data
public class TemperatureMeasurementParam {

    /**
     * 设备标识
     */
    String equipmentIdentification;

    String equipmentId;
    /**
     * 电话号码
     */
    String callNum;
    /**
     * 姓名
     */
    String name;
    /**
     * 身份证
     */
    String card;
    /**
     * 表示
     */
    Integer mark;



    /**
     * 开始时间
     */
    String startTime;
    /**
     * 结束时间
     */
    String endTime;

    /**
     * 考勤组
     */
    String attendanceGroupId;
}
