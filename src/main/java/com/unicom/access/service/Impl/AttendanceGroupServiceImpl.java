package com.unicom.access.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.unicom.access.entity.AttendanceGroup;
import com.unicom.access.mapper.AttendanceGroupMapper;
import com.unicom.access.service.AttendanceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mrChen
 * @date 2021/2/1 13:19
 */
@Component
public class AttendanceGroupServiceImpl implements AttendanceGroupService {
    @Autowired
    AttendanceGroupMapper attendanceGroupMapper;


    @Override
    public AttendanceGroup selectByGroupId() {
        return attendanceGroupMapper.selectOne(new AttendanceGroup());
    }

    @Override
    public void update(AttendanceGroup atp) {
        AttendanceGroup attendanceGroup = attendanceGroupMapper.selectOne(new AttendanceGroup());
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("group_id", attendanceGroup.getAttendanceGroupId());
        attendanceGroupMapper.update(atp, entityWrapper);
    }
}
