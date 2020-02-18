package com.fng.mapper;

import com.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into USER (name,account_id,token,gmtCreate,gmtModified,avatar_url,bio) values (#{name},#{account_id},#{token},#{gmtCreate},#{gmtModified},#{avatar_url},#{bio}) ")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id=${account_id}")
    User findByAccountId(@Param("account_id") String account_id);

    @Update("update user set name=#{name},token=#{token},gmtModified=#{gmtModified},bio=#{bio},avatar_url=#{avatar_url}")
    void update(User User);
}
