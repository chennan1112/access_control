package com.unicom.access.controller;

import com.unicom.access.entity.LoginUser;
import com.unicom.access.entity.TimeTask;
import com.unicom.access.entity.UserLoginEntity;
import com.unicom.access.entity.ViewParam;
import com.unicom.access.service.LoginUserService;
import com.unicom.access.service.NetAccessService;
import com.unicom.access.tool.AesEncryptUtils;
import com.unicom.access.tool.HttpResult;
import com.unicom.access.tool.JwtTool;
import com.unicom.access.tool.smsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author mrChen
 * @date 2021/2/1 19:14
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    final static String templateCode = "5ce2fedc-f415-4dfc-bc72-0acd23fc21f4";

    @Autowired
    NetAccessService netAccessService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    LoginUserService loginUserService;

    /**
     * 验证码
     *
     * @param viewParam
     * @return
     */
    @RequestMapping("/message")
    public HttpResult sengMessage(@RequestBody ViewParam viewParam) {
        String[] phones = new String[]{viewParam.getCallNum()};
        HashMap map = new HashMap();
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        map.put("Code1", verifyCode);
        try {
            String getStr = stringRedisTemplate.opsForValue().get(viewParam.getCallNum());
            if (!StringUtils.isEmpty(getStr)) {
                Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(viewParam.getCallNum());
                return new HttpResult(201, "操作频繁,稍后重试", expire);
            }
            stringRedisTemplate.opsForValue().set(viewParam.getCallNum(), verifyCode, 1000 * 60 * 5, TimeUnit.MILLISECONDS);
            /*发送验证码*/
            boolean b = smsUtils.sendMsg(phones, templateCode, map);
            //boolean b = true;
            if (b) {
                return new HttpResult(200, "验证码发送成功", null);
            } else {
                return new HttpResult(500, "验证码发送失败,或次数达到上限", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResult(500, "服务异常,请重试", null);
        }
    }

    /**
     * 登录
     *
     * @param viewParam
     * @return
     */
    @RequestMapping("/getToken")
    public HttpResult getToken(@RequestBody ViewParam viewParam) {
        try {
            String verifyCode = stringRedisTemplate.opsForValue().get(viewParam.getCallNum());
            if (StringUtils.isEmpty(verifyCode)) {
                return new HttpResult(500, "验证码超时,请重试", null);
            }
            if (viewParam.getMessage().equals(verifyCode)) {
                Map ch = new HashMap();
                ch.put("callNum", viewParam.getCallNum());
                String jwt = JwtTool.createJwt(ch, 1000 * 60 * 60);
                return new HttpResult(200, "登录成功", jwt);
            } else {
                return new HttpResult(400, "验证码错误", null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常,请重试", null);
        }
    }

    /**
     * 登录
     *
     * @param loginUser
     * @return
     */
    @RequestMapping("/password")
    public HttpResult getTokenByPc(@RequestBody LoginUser loginUser) {
        try {
            Boolean login = loginUserService.login(loginUser);
            /*对比信息*/
            if (!login) {
                return new HttpResult(201, "账号或密码错误", null);
            }
            return new HttpResult(200, "登录成功", "jwt");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常,请重试", null);
        }
    }

    /**
     * 登录
     *
     * @param loginUser
     * @return
     */
    @RequestMapping("/update")
    public HttpResult update(@RequestBody LoginUser loginUser) {
        try {
            Boolean login = loginUserService.update(loginUser);
            /*对比信息*/
            if (!login) {
                return new HttpResult(201, "账号或密码错误", null);
            }
            return new HttpResult(200, "修改成功成功", "jwt");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResult(500, "服务异常,请重试", null);
        }
    }

    public static void main(String[] args) throws Exception {
        String user = AesEncryptUtils.encrypt("19931222684");
        String pass = AesEncryptUtils.encrypt("chennan@1234");
        System.out.println(user);
        System.out.println(pass);
    }

}
