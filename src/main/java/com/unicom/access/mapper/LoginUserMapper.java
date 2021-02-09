package com.unicom.access.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.unicom.access.entity.LoginUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mrChen
 * @date 2021/2/9 10:30
 */
@Mapper
public interface LoginUserMapper extends BaseMapper<LoginUser> {
}
