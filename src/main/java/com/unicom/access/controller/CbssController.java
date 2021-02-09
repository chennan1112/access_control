package com.unicom.access.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.unicom.access.entity.BodyParamEntity;
import com.unicom.access.entity.BroadbandInformation;
import com.unicom.access.entity.BroadbandQueryEntity;
import com.unicom.access.entity.ViewParam;
import com.unicom.access.tool.CbssCreateBodyJson;
import com.unicom.access.tool.HttpResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mrChen
 * @date 2021/2/4 17:00
 */
@RestController
@RequestMapping("/cbss")
public class CbssController {


    @Value("${cbss.app.id}")
    private String APP_ID;

    @Value("${cbss.app.secret}")
    private String APP_SECRET;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 验证码
     *
     * @param broadbandQueryEntity
     * @return
     */
    @RequestMapping("/get-info")
    public HttpResult sengMessage(@RequestBody BroadbandQueryEntity broadbandQueryEntity) {

        List<BroadbandInformation> broadbandInformationList = new ArrayList<>();

        /*直接走缓存*/
        String ac = stringRedisTemplate.opsForValue().get("cbss_" + broadbandQueryEntity.getCallNum() + "_" + broadbandQueryEntity.getCityCode());
        if (!StringUtils.isEmpty(ac)) {
            JSONArray jsonArray = JSONArray.parseArray(ac);
            jsonArray.forEach(y -> {
                JSON.toJavaObject((JSONObject) y, BroadbandInformation.class);
                broadbandInformationList.add(JSON.toJavaObject((JSONObject) y, BroadbandInformation.class));
            });
            return new HttpResult(200, "查询成功", broadbandInformationList);
        }
        /*走cbss*/
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        /**/
        String TIMESTAMP = time.format(new Date());
        try {
            String TransId = TIMESTAMP.replaceAll("[^\\d.]+", "") + "232992";
            String ll = "APP_ID" + APP_ID + "TIMESTAMP" + TIMESTAMP + "TRANS_ID" + TransId + APP_SECRET;
            String token = DigestUtils.md5DigestAsHex(ll.getBytes());

            BodyParamEntity bodyParamEntity = BodyParamEntity.builder()
                    .appId(APP_ID)
                    .transId(TransId)
                    .timesTamp(TIMESTAMP)
                    .token(token)
                    .serialNumber(broadbandQueryEntity.getCallNum())
                    .province("18")
                    .city(broadbandQueryEntity.getCityCode())
                    .district("18a05u")
                    .channelId("18b212b")
                    .channelType("330")
                    .operatorId("haojz7")
                    .queryType("05").appKey(APP_ID).appTx(TransId).build();
            JSONObject jt = CbssCreateBodyJson.getJson(bodyParamEntity);
            String json = JSONObject.toJSONString(jt);

            JSONArray carIdArray_1 = getCarInfoByCallNum(json);
            if (null == carIdArray_1 || carIdArray_1.size() == 0) {
                return new HttpResult(201, "没有查到个人信息", null);
            }
            String carId = carIdArray_1.getJSONObject(0).getJSONArray("broadeInfo").getJSONObject(0).getString("certNum");

            bodyParamEntity.setSerialNumber(null);
            bodyParamEntity.setQueryType("04");
            bodyParamEntity.setCertTypeCode("02");
            bodyParamEntity.setCertCode(carId);

            JSONObject jt2 = CbssCreateBodyJson.getJson(bodyParamEntity);
            String json_2 = JSONObject.toJSONString(jt2);
            JSONArray carIdArray_2 = getCarInfoByCallNum(json_2);
            if (null == carIdArray_2 || carIdArray_1.size() == 0) {
                return new HttpResult(201, "没有查到个人信息", null);
            }
            JSONArray ja = carIdArray_2.getJSONObject(0).getJSONArray("broadeInfo");

            ja.forEach(x -> {
                String accountNet = ((JSONObject) x).getString("accountNet");
                String installAddress = ((JSONObject) x).getString("installAddress");
                BroadbandInformation broadbandInformation = new BroadbandInformation(accountNet, installAddress,carId,broadbandQueryEntity.getCallNum());
                broadbandInformationList.add(broadbandInformation);
            });
            if (broadbandInformationList.size()!=0) {
                stringRedisTemplate.opsForValue().set("cbss_" + broadbandQueryEntity.getCallNum() + "_" + broadbandQueryEntity.getCityCode(), JSONObject.toJSONString(broadbandInformationList), 1, TimeUnit.DAYS);
            }
            return new HttpResult(200, "查询成功", broadbandInformationList);
        } catch (
                Exception e) {
            e.printStackTrace();
            return new HttpResult(500, "服务异常,请重试", null);
        }

    }

    /**
     * 根据body获取消息体
     *
     * @param body
     * @return
     */
    private JSONArray getCarInfoByCallNum(String body) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        BufferedReader reader = null;
        CloseableHttpResponse execute = null;
        try {
            String url = "http://10.124.150.230:8000/api/microservice/users/userbroadqry/v1";
            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Accept-Encoding", "identity");
            // 创建请求内容
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            execute = httpClient.execute(httpPost);
            String str_1 = EntityUtils.toString(execute.getEntity(), "utf-8");
            JSONObject jt = JSONObject.parseObject(str_1);

            boolean equals = jt.getJSONObject("UNI_BSS_HEAD").getString("RESP_CODE").equals("00000");
            if (!equals) {
                return null;
            }
            JSONObject ju = jt.getJSONObject("UNI_BSS_BODY").getJSONObject("USERBROADQRY_RSP");
            boolean status = ju.getString("STATUS").equals("0000");
            if (!status) {
                return null;
            }
            JSONObject rsp = ju.getJSONObject("RSP");
            boolean rsp_code = rsp.getString("RSP_CODE").equals("0000");
            if (!rsp_code) {
                return null;
            }
            JSONArray data = rsp.getJSONArray("DATA");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
