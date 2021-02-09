package com.unicom.access.service;

import com.unicom.access.entity.UserLog;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/9 12:27
 */
public interface UserLogService {
    /**
     * 查询
     * @param userLog
     * @return
     */
    List<UserLog> select(UserLog userLog);

    Integer insert(UserLog userLog);

}
