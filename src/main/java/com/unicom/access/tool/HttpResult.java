package com.unicom.access.tool;

import java.util.List;

/**
 * 功能描述: 调用接口返回结果
 *
 * @author: vesus
 * @date: 12:24 2018/6/4
 *
 */
public class HttpResult {

    private int code = 200;
    private String msg;
    private Object data;

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public HttpResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static HttpResult error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static HttpResult error(String msg) {
        return error(500, msg);
    }

    public static HttpResult error(Object data) {
        HttpResult r = new HttpResult();
        r.setCode(500);
        if (data instanceof List){
            r.setMsg(String.valueOf( ((List) data).size()));
        }

        r.setData(data);
        return r;
    }
    public static HttpResult error(int code, String msg) {
        HttpResult r = new HttpResult();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static HttpResult ok(String msg) {
        HttpResult r = new HttpResult();
        r.setMsg(msg);
        return r;
    }
    public static HttpResult ok(String msg,Object data) {
        HttpResult r = new HttpResult();
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
    public static HttpResult error(String msg,Object data) {
        HttpResult r = new HttpResult();
        r.setCode(500);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
    public static HttpResult ok(Object data) {
        HttpResult r = new HttpResult();
        r.setData(data);
        return r;
    }

    public HttpResult() {
    }

    public static HttpResult ok() {
        return new HttpResult();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
