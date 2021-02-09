package com.unicom.access.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author mrChen
 * @date 2021/2/1 20:43
 */
public class smsUtils {

    public static boolean sendMsg(String[] phones, String templateCode, HashMap map) {
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;

            try {
                HttpPost httpPost = new HttpPost("https://sms.appusi.com/v2/short-message");
                httpPost.addHeader("Authorization", "yGNVgkQyTHSVvuwsfqBwslIuPhoWlIEEwGTywxZZFtvaWPWYgfWvGaeLItzBsVsa");
                Map param = new HashMap();
                param.put("phone_numbers", phones);
                param.put("template_code", templateCode);
                param.put("parameters", map);
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("Authorization","yGNVgkQyTHSVvuwsfqBwslIuPhoWlIEEwGTywxZZFtvaWPWYgfWvGaeLItzBsVsa");
//                String string = okHttpCli.doPostJson("https://sms.appusi.com/v2/short-message", JSONArray.toJSONString(param),hashMap);
                System.out.println(JSONArray.toJSONString(param));
                StringEntity s = new StringEntity(JSONArray.toJSONString(param), "UTF-8");
                s.setContentType("application/json");
                httpPost.setEntity(s);
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                if (response.getStatusLine().getStatusCode() != 200){
                    return false;
                }
                HttpEntity entity1 = response.getEntity();
                JSONObject user = JSON.parseObject(EntityUtils.toString(entity1));
                return true;
            } finally {
                if (response != null) {
                    response.close();
                }

                if (client != null) {
                    client.close();
                }

            }
        } catch (Exception var14) {
            var14.printStackTrace();
            return false;
        }

    }

    public static void main(String[] args) {
//        String templateCode = "5ce2fedc-f415-4dfc-bc72-0acd23fc21f4";
//        String[] phones=new String[]{"15530135129"};
//        HashMap map = new HashMap();
//        map.put("Code1", "text");
//        boolean b = smsUtils.sendMsgNew(phones, templateCode, map);
//        System.out.println(b);
        for(int i=0;i<100;i++) {
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            System.out.println(verifyCode);
        }

    }
}
