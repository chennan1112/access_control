package com.unicom.access.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author mrChen
 * @date 2021/2/9 12:22
 */
@Data
@Builder
@TableName("user_log")
public class UserLog {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    @TableField("call_num")
    String callNum;
    @TableField("account_id")
    String accountId;
    @TableField("car_id")
    String carId;
    @TableField("switch_type")
    Integer switchType;
    @TableField("switch_task")
    Integer switchTask;
    @TableField("task_time")
    Timestamp taskTime;
    @TableField("create_time")
    Timestamp createTime;
    @TableField("login_time")
    Timestamp loginTime;



    public UserLog(Long id, String callNum, String accountId, String carId, Integer switchType,
                   Integer switchTask, Timestamp taskTime, Timestamp createTime, Timestamp loginTime) {
        this.id = id;
        this.callNum = callNum;
        this.accountId = accountId;
        this.carId = carId;
        this.switchType = switchType;
        this.switchTask = switchTask;
        this.taskTime = taskTime;
        this.createTime = createTime;
        this.loginTime = loginTime;
    }

    @TableField(exist = false)
    Integer pageSize;
    @TableField(exist = false)
    Integer pageNum;

    public UserLog() {
    }

    public UserLog(Long id, String callNum, String accountId, String carId, Integer switchType, Integer switchTask,
                   Timestamp taskTime, Timestamp createTime, Timestamp loginTime, Integer pageSize, Integer pageNum) {
        this.id = id;
        this.callNum = callNum;
        this.accountId = accountId;
        this.carId = carId;
        this.switchType = switchType;
        this.switchTask = switchTask;
        this.taskTime = taskTime;
        this.createTime = createTime;
        this.loginTime = loginTime;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}
