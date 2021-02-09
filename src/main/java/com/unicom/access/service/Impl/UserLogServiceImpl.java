package com.unicom.access.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.unicom.access.entity.UserLog;
import com.unicom.access.mapper.UserLogMapper;
import com.unicom.access.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/9 12:28
 */
@Component
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    UserLogMapper userLogMapper;

    @Override
    public List<UserLog> select(UserLog userLog) {
        EntityWrapper<UserLog> wrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(userLog.getCarId())) {
            wrapper.eq("car_id", userLog.getCarId());
        }
        if (!StringUtils.isEmpty(userLog.getCallNum())) {
            wrapper.eq("call_num", userLog.getCallNum());
        }
        Page<UserLog> page=new Page<>(userLog.getPageNum(),userLog.getPageSize());

        List<UserLog> userLogs = userLogMapper.selectPage(page, wrapper);
        return userLogMapper.selectPage(page,wrapper);
    }

    @Override
    public Integer insert(UserLog userLog) {
        return userLogMapper.insert(userLog);
    }
}
