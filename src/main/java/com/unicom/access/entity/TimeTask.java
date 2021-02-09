package com.unicom.access.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author mrChen
 * @date 2021/2/3 10:43
 */
@Data
@TableName("time_task")
public class TimeTask {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    @TableField("account_id")
    String accountId;
    @TableField("week_day")
    String weekDay;
    @TableField("start_time")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    Timestamp startTime;
    @TableField("end_time")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    Timestamp endTime;
    @TableField("status")
    Integer status;
    @TableField("is_delete")
    Integer isDelete;

    @TableField("create_time")
    Timestamp createTime;
    @TableField("update_time")
    Timestamp updateTime;


    /**
     * 登录时间
     */
    @TableField(exist = false)
    String loginTime;
    /**
     * 登录时间
     */
    @TableField(exist = false)
    String carId;

    @TableField(exist = false)
    String callNum;
}
