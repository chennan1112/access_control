package com.unicom.access.entity;

import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/7 16:46
 */
@Data
public class BroadbandQueryEntity {
    /**
     * 手机号
     */
    String callNum;
    /**
     * 地级行政区号
     */
    String cityCode;
}
