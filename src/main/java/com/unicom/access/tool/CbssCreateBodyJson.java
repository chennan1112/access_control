package com.unicom.access.tool;

import com.alibaba.fastjson.JSONObject;
import com.unicom.access.entity.BodyParamEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mrChen
 * @date 2021/2/7 9:27
 */
public class CbssCreateBodyJson {

    /**
     * 获取head
     *
     * @param bodyParamEntity
     * @return
     */
    private static JSONObject getHeadJsonObject(BodyParamEntity bodyParamEntity) {
        JSONObject uh = new JSONObject();
        uh.put("APP_ID", bodyParamEntity.getAppId());
        uh.put("TIMESTAMP", bodyParamEntity.getTimesTamp());
        uh.put("TRANS_ID", bodyParamEntity.getTransId());
        uh.put("TOKEN", bodyParamEntity.getToken());
        return uh;
    }

    /**
     * 获取Body
     *
     * @param bodyParamEntity
     * @return
     */
    private static JSONObject getBodyJsonObject_1(BodyParamEntity bodyParamEntity) {
        /*"UNI_BSS_BODY"*/
        JSONObject ub = new JSONObject();
        JSONObject ub_ur = new JSONObject();
        ub_ur.put("APPKEY", bodyParamEntity.getAppKey());
        ub_ur.put("APPTX", bodyParamEntity.getAppTx());
        JSONObject ub_ur_msg = new JSONObject();
        if(!StringUtils.isEmpty(bodyParamEntity.getSerialNumber())) {
            ub_ur_msg.put("serialNumber", bodyParamEntity.getSerialNumber());
        }
        ub_ur_msg.put("province", bodyParamEntity.getProvince());
        ub_ur_msg.put("city", bodyParamEntity.getCity());
        ub_ur_msg.put("district", bodyParamEntity.getDistrict());
        ub_ur_msg.put("channelType", bodyParamEntity.getChannelType());
        ub_ur_msg.put("operatorId", bodyParamEntity.getOperatorId());
        ub_ur_msg.put("channelId", bodyParamEntity.getChannelId());
        ub_ur_msg.put("queryType", bodyParamEntity.getQueryType());
        if(!StringUtils.isEmpty(bodyParamEntity.getCertCode())){
            ub_ur_msg.put("certCode", bodyParamEntity.getCertCode());
        }
        if(!StringUtils.isEmpty(bodyParamEntity.getCertTypeCode())){
            ub_ur_msg.put("certTypeCode", bodyParamEntity.getCertTypeCode());
        }
        ub_ur.put("MSG", ub_ur_msg);
        ub.put("USERBROADQRY_REQ", ub_ur);
        return ub;
    }

    /**
     * 获取ATTACHED
     *
     * @param bodyParamEntity
     * @return
     */
    private static JSONObject getAttachedJsonObject(BodyParamEntity bodyParamEntity) {

        /*UNI_BSS_ATTACHED*/
        JSONObject ua = new JSONObject();
        ua.put("MEDIA_INFO", "");
        return ua;
    }


    public static JSONObject getJson(BodyParamEntity bodyParamEntity) {

        /*UNI_BSS_ATTACHED*/
        JSONObject jt = new JSONObject();
        jt.put("UNI_BSS_HEAD", getHeadJsonObject(bodyParamEntity));
        jt.put("UNI_BSS_ATTACHED", getAttachedJsonObject(bodyParamEntity));
        jt.put("UNI_BSS_BODY", getBodyJsonObject_1(bodyParamEntity));
        return jt;

    }

}
