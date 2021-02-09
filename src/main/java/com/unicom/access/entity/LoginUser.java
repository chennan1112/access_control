package com.unicom.access.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/9 10:26
 */
@Data
@TableName("login_user")
public class LoginUser {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    @TableField("call_num")
    String callNum;
    @TableField("password")
    String password;
    @TableField("is_delete")
    Integer isDelete;
    @TableField(exist = false)
    String prePassword;
}
