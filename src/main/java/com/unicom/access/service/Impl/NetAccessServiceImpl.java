package com.unicom.access.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.unicom.access.entity.NetAccess;
import com.unicom.access.mapper.NetAccessMapper;
import com.unicom.access.service.NetAccessService;
import org.apache.log4j.helpers.QuietWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mrChen
 * @date 2021/2/2 9:43
 */
@Component
public class NetAccessServiceImpl implements NetAccessService {

    @Autowired
    NetAccessMapper netAccessMapper;

    @Override
    public List<NetAccess> selectByCallNum(Integer callName) {
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("call_unm",callName);
        try {
            return netAccessMapper.selectList(entityWrapper);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
