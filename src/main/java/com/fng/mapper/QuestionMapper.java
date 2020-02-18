package com.fng.mapper;

import com.fng.dto.QuestionDTO;
import com.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmtcreate,gmtmodified,creator,tag) values(#{title},#{description},#{gmtcreate},#{gmtmodified},#{creator},#{tag})")
     void insertQuestion(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset,@Param("size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param("offset")Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creator= #{userId} ")
    Integer countById(@Param("userId")Integer userId);

    @Select("select * from question where id = ${id}")
    Question getById(@Param("id") Integer id);
}
