package com.unicom.access.service;

import com.unicom.access.entity.NetAccess;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/2 9:41
 */
public interface NetAccessService {
    List<NetAccess> selectByCallNum(Integer callName);
}
