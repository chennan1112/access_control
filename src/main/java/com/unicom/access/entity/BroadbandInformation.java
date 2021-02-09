package com.unicom.access.entity;

import lombok.Data;

/**
 * @author mrChen
 * @date 2021/2/5 14:30
 */
@Data
public class BroadbandInformation {
    public BroadbandInformation() {
    }

    /**
     * 账号
     */
    String accountId;
    /**
     * 地址
     */
    String address;
    /**
     *
     */
    String carId;

    public BroadbandInformation(String accountId, String address, String carId, String callNum) {
        this.accountId = accountId;
        this.address = address;
        this.carId = carId;
        this.callNum = callNum;
    }

    /**
     *
     */
    String callNum;

}
