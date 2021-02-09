package com.unicom.access.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.unicom.access.tool.HttpResult;
import com.unicom.access.tool.JwtTool;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mrChen
 * @date 2021/2/1 21:12
 */
public class TokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    public TokenInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        this.logger.info("======================TokenHandlerInterceptor=============");

        if (request.getRequestURI().equals("/login/message") || request.getRequestURI().equals("/login/getToken")) {
            return true;
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String tokenStr = request.getHeader("authorization");
        if (StringUtils.isEmpty(tokenStr) || !tokenStr.startsWith("Bearer")) {
            HttpResult httpResult = new HttpResult(401, "examine token", null);
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.toJSONString(httpResult));
            return false;
        }
        try {
            String[] bearer_s = tokenStr.split("earer ");
            Claims claims = JwtTool.parseJwt(bearer_s[1]);
            String callNum = (String) claims.get("callNum");
            String bodyString = HttpHelper.getBodyString(request);
            JSONObject jsonObject = JSONObject.parseObject(bodyString);
            if(!StringUtils.isEmpty(jsonObject.getString("callNum"))){
                if(!jsonObject.getString("callNum").equals(callNum)){
                    HttpResult httpResult =new HttpResult(401,"检查手机号正确性",null);
                    PrintWriter writer = response.getWriter();
                    writer.write(JSONObject.toJSONString(httpResult));
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            this.logger.info("======================TOKEN校验失败=============");
            HttpResult httpResult = new HttpResult(401, "检查token有效性", null);
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.toJSONString(httpResult));
            return false;
        }
    }

    // 二进制读取
    public static byte[] readAsBytes(HttpServletRequest request) {

        int len = request.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = null;

        try {
            in = request.getInputStream();
            in.read(buffer, 0, len);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
}
