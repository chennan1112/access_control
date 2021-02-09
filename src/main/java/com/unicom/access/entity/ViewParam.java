package com.unicom.access.entity;

import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/2 15:04
 */
@Data
public class ViewParam {
    /**
     * 手机号
     */
    String callNum;
    /**
     * 验证码
     */
    String message;
    /**
     * 账号
     */
    String accountId;
    /**
     * 开始时间
     */
    String startTime;
    /**
     * 结束时间
     */
    String endTime;
    /**
     * 周几
     */
    String weekDay;

    /**
     * 登录时间
     */
    String loginTime;
    /**
     * 登录时间
     */
    String carId;

}
