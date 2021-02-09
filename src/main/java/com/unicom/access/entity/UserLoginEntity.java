package com.unicom.access.entity;

import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/8 10:01
 */
@Data
public class UserLoginEntity {
    /**
     * 手机号
     */
    String callNum;
    /**
     * 密码
     */
    String passWord;
}
