package com.unicom.access.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unicom.access.conf.TokenInterceptor;
import com.unicom.access.entity.AttendanceGroup;
import com.unicom.access.entity.TemperatureMeasurementParam;
import com.unicom.access.entity.TemperatureMeasurementResult;
import com.unicom.access.service.AttendanceGroupService;
import com.unicom.access.tool.HttpClientUtil;
import com.unicom.access.tool.HttpResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mrChen
 * @date 2021/1/29 17:42
 */
@RestController
@RequestMapping("/temperature")
public class TemperatureMeasurementController {

    private static Logger log = LoggerFactory.getLogger(TemperatureMeasurementController.class);
    private static String LOG_PR = "门禁测温服务";
    @Autowired
    AttendanceGroupService attendanceGroupService;

    @RequestMapping("/select")
    public HttpResult obtainData(@RequestBody TemperatureMeasurementParam temperatureMeasurementParam) {

        if (null == temperatureMeasurementParam || null == temperatureMeasurementParam.getMark()
                || (temperatureMeasurementParam.getMark() != 1 && temperatureMeasurementParam.getMark() != 2 && temperatureMeasurementParam.getMark() != 3)
                || null == temperatureMeasurementParam.getEquipmentIdentification()) {
            return new HttpResult(400, "请检查入参", null);
        }

        TemperatureMeasurementResult tem = new TemperatureMeasurementResult();
        tem.setEquipmentIdentification(temperatureMeasurementParam.getEquipmentIdentification());

        /*调用钉钉接口*/
        if (temperatureMeasurementParam.getMark() == 3) {
            if (null == temperatureMeasurementParam.getCallNum() ||
                    null == temperatureMeasurementParam.getStartTime() ||
                    null == temperatureMeasurementParam.getEndTime()) {
                return new HttpResult(400, "设备标识或者电话号码或时间不能为空", null);
            }
            String token = getToken();
            if (null == token) {
                return new HttpResult(500, "钉钉token接口异常,请重试", null);
            }
            /*获取userid*/
            String userId = getUserId(token, temperatureMeasurementParam.getCallNum());
            AttendanceGroup attendanceGroup = attendanceGroupService.selectByGroupId();
            Boolean aBoolean;
            if (null != temperatureMeasurementParam.getAttendanceGroupId()) {
                aBoolean = attendanceGroup(token, userId, tem, temperatureMeasurementParam.getAttendanceGroupId());
            } else {
                aBoolean = attendanceGroup(token, userId, tem, attendanceGroup.getAttendanceGroupId());
            }
            if (null == aBoolean) {
                return new HttpResult(500, "钉钉获取考勤组失败", null);
            } else if (false == aBoolean) {
                return new HttpResult(201, "没有在考勤组内", null);
            }

            /*获取姓名和身份证*/
            String[] card = getCard(token, userId);
            if (null == card || null == card[0] || null == card[1]) {
                return new HttpResult(500, "钉钉获取姓名或身份证失败,请联系钉钉管理员", null);
            }
            tem.setName(card[0]);
            //tem.setCard(card[1]);
            /*获取考勤*/
            List<String> attendance = getAttendance(token, userId, temperatureMeasurementParam.getStartTime(), temperatureMeasurementParam.getEndTime());
            if (null == attendance) {
                return new HttpResult(500, "钉钉获取考勤失败 或者时间跨度过大", null);
            }
            tem.setAttendance(attendance);
            /*获取健康码*/
            String jkm = getJKM(card[0], card[1]);
            if (StringUtils.isEmpty(jkm)) {
                return new HttpResult(500, "获取健康码失败,请重试", null);
            }
            tem.setJkm(jkm);
            return new HttpResult(200, "获取健康码成功", tem);
        }
        /*直接调用健康码接口*/
        if (temperatureMeasurementParam.getMark() == 2) {
            if (null == temperatureMeasurementParam.getName() ||
                    null == temperatureMeasurementParam.getCard()) {
                return new HttpResult(400, "姓名或者身份证不能为空", null);
            }
            String jkm = getJKM(temperatureMeasurementParam.getName(), temperatureMeasurementParam.getCard());
            if (StringUtils.isEmpty(jkm)) {
                return new HttpResult(500, "获取健康码失败,请重试", null);
            }
            tem.setName(temperatureMeasurementParam.getName());
            tem.setJkm(jkm);
            tem.setEquipmentIdentification(temperatureMeasurementParam.getEquipmentIdentification());
            return new HttpResult(200, "获取健康码成功", tem);
        }
        /*调用钉钉接口*/
        if (temperatureMeasurementParam.getMark() == 1) {
            if (null == temperatureMeasurementParam.getCallNum() ||
                    null == temperatureMeasurementParam.getStartTime() ||
                    null == temperatureMeasurementParam.getEndTime()) {
                return new HttpResult(400, "设备标识或者电话号码或时间不能为空", null);
            }
            String token = getToken();
            if (null == token) {
                return new HttpResult(500, "钉钉token接口异常,请重试", null);
            }
            /*获取userid*/
            String userId = getUserId(token, temperatureMeasurementParam.getCallNum());

            /*获取姓名和身份证*/
            String[] card = getCard(token, userId);
            if (null == card || null == card[0] || null == card[1]) {
                return new HttpResult(500, "钉钉获取姓名或身份证失败,请联系钉钉管理员", null);
            }
            tem.setName(card[0]);
            //tem.setCard(card[1]);
            /*获取考勤*/
            List<String> attendance = getAttendance(token, userId, temperatureMeasurementParam.getStartTime(), temperatureMeasurementParam.getEndTime());
            if (null == attendance) {
                return new HttpResult(500, "钉钉获取考勤失败 或者时间跨度过大", null);
            }
            tem.setAttendance(attendance);
            /*获取健康码*/
            String jkm = getJKM(card[0], card[1]);
            if (StringUtils.isEmpty(jkm)) {
                return new HttpResult(500, "获取健康码失败,请重试", null);
            }
            tem.setJkm(jkm);
            return new HttpResult(200, "获取健康码成功", tem);

        }
        return null;
    }

    private  String getToken() {
        Map tokenParm = new HashMap<>();
        tokenParm.put("appkey", "ding8ba6leoupn8g0zpu");
        tokenParm.put("appsecret", "cuJcACIfTFJLBA63RgiXY_FaE1OVKd2yj1_p06bmMkjJF-BVs4lPF0r2NOzfpD9Z");
        try {
            String tokenJson = HttpClientUtil.doGet("https://oapi.dingtalk.com/gettoken", tokenParm);
            JSONObject jsonObject = JSONObject.parseObject(tokenJson);
            String accessToken = jsonObject.getString("access_token");
            log.info("门禁测温服务 获取token:成功======================");
            return accessToken;
        } catch (Exception ex) {
            log.info("门禁测温服务 获取token:失败" + ex);
            return null;
        }
    }

    private static String getUserId(String token, String mobile) {
        //获取uerid
        Map userIdParam = new HashMap<>();
        userIdParam.put("access_token", token);
        userIdParam.put("mobile", mobile);
        try {
            String s = HttpClientUtil.doPost("https://oapi.dingtalk.com/topapi/v2/user/getbymobile", userIdParam);
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            JSONObject result = jsonObject1.getJSONObject("result");
            String userid = result.getString("userid");
              log.info("门禁测温服务 userid:成功======================");
            return userid;
        } catch (Exception ex) {
            log.info("门禁测温服务 获取userId失败:" + ex);
            return null;
        }
    }

    private static String[] getCard(String accessToken, String userid) {
        Map parm2 = new HashMap<>();
        parm2.put("userid_list", userid);
        parm2.put("agentid", 1086990183l);
        parm2.put("field_filter_list", "sys02-certNo,sys02-realName");
        String ss = JSONObject.toJSONString(parm2);
        try {
            String url1 = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/v2/list?access_token=" + accessToken;
            String result = HttpClientUtil.doPostJson(url1, ss);
            if (StringUtils.isEmpty(result)) {

            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            String errmsg = jsonObject.getString("errmsg");
            Integer errcode = jsonObject.getInteger("errcode");
            if (!errmsg.equals("ok") || errcode != 0) {

            }
            JSONObject jn = jsonObject.getJSONArray("result").getJSONObject(0);
            JSONArray jsonArray = jn.getJSONArray("field_data_list");
            String[] rt = new String[2];

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jt = jsonArray.getJSONObject(i);
                /*姓名*/
                if (jt.getString("field_code").equals("sys02-realName")) {
                    JSONArray field_value_list = jt.getJSONArray("field_value_list");
                    JSONObject jc = field_value_list.getJSONObject(0);
                    String value = jc.getString("value");
                    rt[0] = value;
                }
                /*身份证号码*/
                if (jt.getString("field_code").equals("sys02-certNo")) {
                    JSONArray field_value_list = jt.getJSONArray("field_value_list");
                    JSONObject jc = field_value_list.getJSONObject(0);
                    String value = jc.getString("value");
                    rt[1] = value;
                }
            }
            log.info("门禁测温服务 获取身份证:成功======================");
            return rt;
        } catch (Exception ex) {
            log.info("门禁测温服务 获取姓名 身份证失败:" + ex);
            return null;
        }
    }

    private static List<String> getAttendance(String accessToken, String userIdList, String start, String end) {

        List<String> result = new ArrayList<>();

        Map attendanceParam = new HashMap<>();
        attendanceParam.put("workDateFrom", start);
        attendanceParam.put("offset", 0l);
        attendanceParam.put("limit", 2);
        String kl = "[" + "\"" + userIdList + "\"" + "]";
        attendanceParam.put("userIdList", kl);
        attendanceParam.put("workDateTo", end);
        String string = JSONObject.toJSONString(attendanceParam);
        try {
            String url = "https://oapi.dingtalk.com/attendance/list?access_token=" + accessToken;
            String s3 = HttpClientUtil.doPostJson(url, string);
            JSONObject jt = JSONObject.parseObject(s3);
            if (null == jt || !jt.getString("errcode").equals("0") || !jt.getString("errmsg").equals("ok")) {
                return null;
            }
            JSONArray recordArray = jt.getJSONArray("recordresult");
            for (int k = 0; k < recordArray.size(); k++) {
                JSONObject jo = recordArray.getJSONObject(k);
                String timeResult = jo.getString("timeResult");
                result.add(timeResult);
            }
            log.info("门禁测温服务 获取考勤:成功======================");
            return result;
        } catch (Exception ex) {
            log.info("门禁测温服务 获取考勤信息失败:" + ex);
            return null;
        }

    }

    /**
     * 获取健康码
     *
     * @param name
     * @param card
     * @return
     */
    private static String getJKM(String name, String card) {
        Map jkmMap = new HashMap<>();
        jkmMap.put("name", name);
        jkmMap.put("card", card);
        try {
            String jkmResult = HttpClientUtil.doGet("http://jkm.hb10.net/hbjkm/v1/query", jkmMap);
            if (null == jkmResult) {
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(jkmResult);
            String msg = jsonObject.getString("msg");
            if (msg.equals("成功")) {
                return jsonObject.getJSONObject("data").getString("result");
            }
            log.info("门禁测温服务 获取健康码:成功======================");
            return null;
        } catch (Exception e) {
            log.info("门禁测温服务 获取考勤信息失败:" + e);
            return null;
        }
    }

    /**
     * 判断是不是在指定的考勤组
     *
     * @param accessToken
     * @param userId
     * @param gi
     * @return
     */
    private Boolean attendanceGroup(String accessToken, String userId, TemperatureMeasurementResult tm, String gi) {
        Map attendanceParam = new HashMap<>();
        attendanceParam.put("userid", userId);
        String string = JSONObject.toJSONString(attendanceParam);
        try {
            String url = "https://oapi.dingtalk.com/topapi/attendance/getusergroup?access_token=" + accessToken;
            String s3 = HttpClientUtil.doPostJson(url, string);
            JSONObject jt = JSONObject.parseObject(s3);
            if (null == jt || !jt.getString("errcode").equals("0")) {
                return false;
            }
            String aLong = jt.getJSONObject("result").getString("group_id");
            String name = jt.getJSONObject("result").getString("name");
            if (aLong.equals(gi)) {
                tm.setAttendanceGroupName(name);
                tm.setAttendanceGroupId(aLong);
                log.info("门禁测温服务 获取考勤组:成功======================");
                return true;
            }
            return false;

        } catch (Exception ex) {
            log.info("门禁测温服务 获取考勤组信息失败:" + ex);
            return null;
        }
    }
}
