package com.unicom.access.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/1/29 21:07
 */
@Data
public class TemperatureMeasurementResult {


    /**
     * 姓名
     */
    String name;
    /**
     * 身份证号码
     */
    String card;
    /**
     * 健康码
     */
    String jkm;
    /**
     * 设备标识
     */
    String equipmentIdentification;
    /**
     * 考勤
     */
    List<String> attendance;
    /**
     * 考勤组编码
     */
    String attendanceGroupId;
    /**
     * 考勤组名称
     */
    String attendanceGroupName;
}
