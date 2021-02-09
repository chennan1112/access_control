package com.unicom.access.service;

import com.unicom.access.entity.AttendanceGroup;

/**
 * @author mrChen
 * @date 2021/2/1 13:16
 */
public interface AttendanceGroupService {

    AttendanceGroup selectByGroupId();

    void update(AttendanceGroup atp);
}
