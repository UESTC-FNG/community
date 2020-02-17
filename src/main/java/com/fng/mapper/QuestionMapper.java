package com.fng.mapper;

import com.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmtcreate,gmtmodified,creator,tag) values(#{title},#{description},#{gmtcreate},#{gmtmodified},#{creator},#{tag})")
     void insertQuestion(Question question);

    @Select("select * from question")
    List<Question> list();
}
