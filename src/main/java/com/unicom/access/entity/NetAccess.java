package com.unicom.access.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/2 9:38
 */
@Data
@TableName("net_access")
public class NetAccess {
    @TableId
    private Integer id;
    @TableField("user_name")
    private String userName;
    @TableField("call_num")
    private Integer callNum;
    @TableField("verify_code")
    private Integer verifyCode;
    @TableField("is_delete")
    private Integer isDelete;
}
