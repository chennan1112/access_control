package com.unicom.access.controller;

import com.unicom.access.entity.UserLog;
import com.unicom.access.service.UserLogService;
import com.unicom.access.tool.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/9 12:25
 */
@RestController
@RequestMapping("/log")
public class UserLogController {

    @Autowired
    UserLogService userLogService;

    /**
     * 验证码
     *
     * @param userLog
     * @return
     */
    @RequestMapping("/select")
    public HttpResult sengMessage(@RequestBody UserLog userLog) {
        try {
            List<UserLog> select = userLogService.select(userLog);
            if (null == select || select.size() == 0) {
                return new HttpResult(201, "查询数据为空", null);
            }
            return new HttpResult(200, "查询成功", select);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResult(500, "服务异常,请重试", null);
        }
    }

}
