package com.fng.service;


import com.fng.dto.PageDTO;
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



    public PageDTO list(Integer page, Integer size) {
        if (page<1){
            page=1;
        }
        Integer totalCount = questionMapper.count();
        Integer totalPage=0;
        if (totalCount/5==0){
            totalPage=totalCount/5;
        }else{
            totalPage=totalCount/5+1;
        }
        if (page>totalPage){
            page=totalPage;
        }


        //定义偏移量
        Integer offset=(page-1)*size;

        //通过questionMapper获得question list
        List<Question> questionList = questionMapper.list(offset,size);
        //根据question获得user
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        PageDTO pageDTO=new PageDTO();
        for (Question question:questionList){
            User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }
}
