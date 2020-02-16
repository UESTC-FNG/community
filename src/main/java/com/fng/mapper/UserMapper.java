package com.fng.mapper;

import com.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into USER (name,account_id,token,gmtCreate,gmtModified) values (#{name},#{account_id},#{token},#{gmtCreate},#{gmtModified}) ")
    void insert(User user);

}
