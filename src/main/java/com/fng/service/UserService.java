package com.fng.service;

import com.fng.mapper.UserMapper;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
    //先查找数据库中是否存在该用户的account_id
       User dbUser = userMapper.findByAccountId(user.getAccount_id());
       if (dbUser==null){
           //插入数据库
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
       }else{
           //更新token
           dbUser.setGmtModified(System.currentTimeMillis());
           dbUser.setAvatar_url(user.getAvatar_url());
           dbUser.setBio(user.getBio());
           dbUser.setToken(user.getToken());
           dbUser.setName(user.getName());
           userMapper.update(dbUser);
       }
    }
}
