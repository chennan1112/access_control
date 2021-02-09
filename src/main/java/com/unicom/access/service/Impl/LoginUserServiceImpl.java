package com.unicom.access.service.Impl;

import com.unicom.access.entity.LoginUser;
import com.unicom.access.mapper.LoginUserMapper;
import com.unicom.access.service.LoginUserService;
import com.unicom.access.tool.AesEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mrChen
 * @date 2021/2/9 10:32
 */
@Component
public class LoginUserServiceImpl implements LoginUserService {

    @Autowired
    LoginUserMapper loginUserMapper;

    @Override
    public Boolean login(LoginUser loginUser) {
        LoginUser l=new LoginUser();
        l.setCallNum(loginUser.getCallNum());
        LoginUser db_user = loginUserMapper.selectOne(loginUser);
        try {
            String db_p = AesEncryptUtils.decrypt(db_user.getPassword());
            String vi_p =AesEncryptUtils.decrypt(loginUser.getPassword());
            if(db_p.equals(vi_p)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(LoginUser loginUser) {

        /*检查原密码密码*/
        LoginUser l=new LoginUser();
        l.setCallNum(loginUser.getCallNum());
       // l.setId(loginUser.getId());
        LoginUser db_user = loginUserMapper.selectOne(l);
        try {
            String db_p = AesEncryptUtils.decrypt(db_user.getPassword());
            String vi_p =AesEncryptUtils.decrypt(loginUser.getPrePassword());
            if(db_p.equals(vi_p)){
                loginUser.setId(db_user.getId());
                loginUserMapper.updateById(loginUser);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
