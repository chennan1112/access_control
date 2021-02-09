package com.unicom.access.service;

import com.unicom.access.entity.LoginUser;

/**
 * @author mrChen
 * @date 2021/2/9 10:31
 */
public interface LoginUserService {
    public Boolean login(LoginUser loginUser);
    public Boolean update(LoginUser loginUser);
}
