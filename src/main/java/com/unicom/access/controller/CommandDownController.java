package com.unicom.access.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.unicom.access.entity.TimeTask;
import com.unicom.access.entity.UserLog;
import com.unicom.access.entity.ViewParam;
import com.unicom.access.service.TimeTaskService;
import com.unicom.access.service.UserLogService;
import com.unicom.access.tool.HttpClientUtil;
import com.unicom.access.tool.HttpGetWithEntity;
import com.unicom.access.tool.HttpResult;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mrChen
 * @date 2021/2/2 11:47
 */
@RestController
@RequestMapping("/command")
public class CommandDownController {

    /*查询状态*/
    @Value("${status.url}")
    private String status_url;
    /*加锁*/
    @Value("${lock.url}")
    private String lock_url;
    /*解锁*/
    @Value("${unlock.url}")
    private String unlock_url;

    @Autowired
    UserLogService userLogService;


    @Autowired
    TimeTaskService timeTaskService;

    @RequestMapping("/getStatus")
    public HttpResult getStatus(@RequestBody ViewParam viewParam) {
        Map<String, String> param = new HashMap<>();
        param.put("accountId", viewParam.getAccountId());
        try {
            String statusResult = HttpClientUtil.doGetJson(status_url, JSONObject.toJSONString(viewParam));
            JSONObject jt = JSONObject.parseObject(statusResult);
            /*查询状态的结果*/
            Integer resultCode = jt.getInteger("resultCode");
            String resultMsg = jt.getString("resultMsg");
            /*查询失败*/
            if (resultCode.intValue() == 1) {
                String msg = "";
                switch (resultMsg) {
                    case "101":
                        msg = "账号不存在";
                        break;
                    case "103":
                        msg = "其他错误";
                        break;
                }
                return new HttpResult(201, msg, null);
            }
            /*查询成功*/
            if (resultCode.intValue() == 0) {
                JSONObject accountStatus = jt.getJSONObject("accountStatus");
                String cbStatus = accountStatus.getString("cbStatus");
                /*状态异常*/
                if (cbStatus.equals("1")) {
                    return new HttpResult(201, "状态异常", null);
                }
                /*状态正常*/
                if (cbStatus.equals("0")) {
                    String lockStatus = accountStatus.getString("lockStatus");
                    String msg = "";
                    switch (lockStatus) {
                        case "0":
                            msg = "未锁定";
                            break;
                        case "1":
                            msg = "已锁定";
                            break;
                    }
                    return new HttpResult(200, msg, null);
                }
                return new HttpResult(500, "下游接口变动", null);
            }
            return new HttpResult(500, "下游接口变动", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常,稍后重试", null);
        }
    }

    @RequestMapping("/lock")
    public HttpResult lock(@RequestBody ViewParam viewParam) {

        UserLog u = UserLog.builder().carId(viewParam.getCarId()).accountId(viewParam.getAccountId()).callNum(viewParam.getCallNum())
                .loginTime(Timestamp.valueOf(viewParam.getLoginTime()))
                .createTime(new Timestamp(System.currentTimeMillis()))
                .switchTask(1)
                .switchType(0)
                .build();
        userLogService.insert(u);

        Map<String, String> lockParam = new HashMap<>();
        lockParam.put("accountId", viewParam.getAccountId());
        try {
            String unLockResult = HttpClientUtil.doPostJson(lock_url, JSONObject.toJSONString(lockParam));
            JSONObject jt = JSONObject.parseObject(unLockResult);
            /*查询状态的结果*/
            Integer resultCode = jt.getInteger("resultCode");
            String resultMsg = jt.getString("resultMsg");
            if (resultCode == 0) {
                return new HttpResult(200, "锁定成功", null);
            }
            String msg = "";
            switch (resultMsg) {
                case "101":
                    msg = "账号不存在";
                    break;
                case "102":
                    msg = "账号已锁定";
                    break;
                case "103":
                    msg = "其他错误";
                    break;
            }
            return new HttpResult(201, msg, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常,稍后重试", null);
        }
    }

    @RequestMapping("/unlock")
    public HttpResult unlock(@RequestBody ViewParam viewParam) {
        UserLog u = UserLog.builder().carId(viewParam.getCarId()).accountId(viewParam.getAccountId()).callNum(viewParam.getCallNum())
                .loginTime(Timestamp.valueOf(viewParam.getLoginTime()))
                .createTime(new Timestamp(System.currentTimeMillis()))
                .switchTask(1)
                .switchType(0)
                .build();
        userLogService.insert(u);
        Map<String, String> param = new HashMap<>();
        param.put("accountId", viewParam.getAccountId());
        try {
            String unLockResult = HttpClientUtil.doPostJson(unlock_url, JSONObject.toJSONString(param));
            JSONObject jt = JSONObject.parseObject(unLockResult);
            /*查询状态的结果*/
            Integer resultCode = jt.getInteger("resultCode");
            String resultMsg = jt.getString("resultMsg");
            if (resultCode == 0) {
                return new HttpResult(200, "解锁成功", null);
            }
            String msg = "";
            switch (resultMsg) {
                case "101":
                    msg = "账号不存在";
                    break;
                case "102":
                    msg = "账号已解锁";
                    break;
                case "103":
                    msg = "其他错误";
                    break;
            }
            return new HttpResult(201, msg, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常,稍后重试", null);
        }
    }

    @RequestMapping("/timing/lock")
    public HttpResult timingLock(@RequestBody TimeTask timeTask) {

        UserLog u = UserLog.builder().carId(timeTask.getCarId()).accountId(timeTask.getAccountId()).callNum(timeTask.getCallNum())
                .loginTime(Timestamp.valueOf(timeTask.getLoginTime()))
                .createTime(new Timestamp(System.currentTimeMillis()))
                .switchTask(0)
                .switchType(1)
                .build();
        userLogService.insert(u);

        try {
            timeTask.setStatus(0);
            timeTask.setIsDelete(0);
            timeTask.setCreateTime(new Timestamp(System.currentTimeMillis()));
            timeTask.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            timeTaskService.insert(timeTask);
            return new HttpResult(200, "任务插入设置成功", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常", null);
        }
    }

    @RequestMapping("/timing/update")
    public HttpResult timingDelete(@RequestBody TimeTask timeTask) {

        UserLog u = UserLog.builder().carId(timeTask.getCarId()).accountId(timeTask.getAccountId()).callNum(timeTask.getCallNum())
                .loginTime(Timestamp.valueOf(timeTask.getLoginTime()))
                .createTime(new Timestamp(System.currentTimeMillis()))
                .switchTask(1)
                .switchType(1)
                .build();
        if(StringUtils.isEmpty(timeTask.getStartTime())){

        }
        if(StringUtils.isEmpty(timeTask.getEndTime())){

        }
        userLogService.insert(u);

        try {
            Integer it = timeTaskService.updateById(timeTask);
            if (it <= 0) {
                return new HttpResult(201, "更新失败", null);
            }
            return new HttpResult(200, "更新成功", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "更新异常", null);
        }
    }

    @RequestMapping("/timing/select")
    public HttpResult timingSelect(@RequestBody TimeTask timeTask) {
        try {
            // TODO 判断时间
            List<TimeTask> timeTasks = timeTaskService.selectAll(timeTask);
            return new HttpResult(200, "查询成功", timeTasks);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常", null);
        }
    }
}
