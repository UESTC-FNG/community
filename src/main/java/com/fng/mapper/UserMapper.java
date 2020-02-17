package com.fng.mapper;

import com.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into USER (name,account_id,token,gmtCreate,gmtModified,avatar_url) values (#{name},#{account_id},#{token},#{gmtCreate},#{gmtModified},#{avatar_url}) ")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
