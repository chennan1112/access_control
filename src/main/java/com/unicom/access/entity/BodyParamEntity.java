package com.unicom.access.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/7 9:09
 */
@Data
@Builder
public class BodyParamEntity {
    /*
    UNI_BSS_HEAD
     */
    String appId;
    String timesTamp;
    String transId;
    String token;
    /*
    UNI_BSS_BODY
    */

    String serialNumber;
    String province;
    String city;
    String district;
    String channelType;
    String operatorId;
    String channelId;
    String queryType;
    String certCode;
    String certTypeCode;
    String appKey;
    String appTx;
}
