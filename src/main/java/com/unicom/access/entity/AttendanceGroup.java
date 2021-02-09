package com.unicom.access.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/1 13:17
 */
@Data
@TableName("attendance_group")
public class AttendanceGroup {
    /**
     * 考勤组编码
     */
    @TableField("group_id")
    String attendanceGroupId;
    /**
     * 考勤组名称
     */
    @TableField("group_name")
    String attendanceGroupName;
}
