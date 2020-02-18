package com.fng.service;

import com.fng.mapper.UserMapper;
import com.fng.model.User;
import com.fng.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
    //先查找数据库中是否存在该用户的account_id
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
        if (users.size()==0){
           //插入数据库
           user.setGmtcreate(System.currentTimeMillis());
           user.setGmtmodified(user.getGmtcreate());
           userMapper.insert(user);
       }else{
           //更新token
            User dbUser=users.get(0);
           User updateUser=new User();
            updateUser.setGmtmodified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setBio(user.getBio());
            updateUser.setToken(user.getToken());
            updateUser.setName(user.getName());
            UserExample example1 = new UserExample();
            example1.createCriteria().andAccountIdEqualTo(dbUser.getAccountId());
            userMapper.updateByExampleSelective(updateUser,example1 );
       }
    }
}
