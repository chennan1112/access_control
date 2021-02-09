package com.unicom.access.controller;

import com.unicom.access.entity.AttendanceGroup;
import com.unicom.access.service.AttendanceGroupService;
import com.unicom.access.tool.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mrChen
 * @date 2021/2/1 15:11
 */
@RestController
@RequestMapping("/group")
public class AttendanceGroupController {

    @Autowired
    AttendanceGroupService attendanceGroupService;

    @RequestMapping("/update")
    public HttpResult update(@RequestBody AttendanceGroup attendanceGroup){
            attendanceGroupService.update(attendanceGroup);
            return new HttpResult(200,"修改成功",null);
    }
}
