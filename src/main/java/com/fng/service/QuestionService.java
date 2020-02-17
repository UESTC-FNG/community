package com.fng.service;


import com.fng.dto.QuestionDTO;
import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.model.Question;
import com.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired(required=false)
    private UserMapper userMapper;

    @Autowired(required=false)
    private QuestionMapper questionMapper;


    public List<QuestionDTO> list() {
        //通过questionMapper获得question list
        List<Question> questionList = questionMapper.list();
        //根据question获得user
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question:questionList){
         User user= userMapper.findById(question.getCreator());
         QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
