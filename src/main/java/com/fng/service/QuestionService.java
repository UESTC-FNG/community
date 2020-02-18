package com.fng.service;


import com.fng.dto.PageDTO;
import com.fng.dto.QuestionDTO;
import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.fng.model.Question;
import com.fng.model.QuestionExample;
import com.fng.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        Integer totalPage=0;
        if (totalCount%5==0){
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
        QuestionExample example = new QuestionExample();
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //根据question获得user
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        PageDTO pageDTO=new PageDTO();
        for (Question question:questions){
            User user= userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public PageDTO list(Integer userId, Integer page, Integer size) {
        if (page<1){
            page=1;
        }
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);


        Integer totalPage=0;
        if (totalCount%5==0){
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
        QuestionExample userExample = new QuestionExample();
        userExample.createCriteria().andCreatorEqualTo(userId);
        RowBounds rowBounds = new RowBounds(offset,size);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(userExample, rowBounds);
        //根据question获得user
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        PageDTO pageDTO=new PageDTO();
        for (Question question:questionList){
            User user= userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id,
                               HttpServletRequest request) {
        //初始化QuestionDTO
        QuestionDTO questionDTO=new QuestionDTO();
        //先调用questionMapper查询question
        Question question = questionMapper.selectByPrimaryKey(id);
        //加入user
        User user = (User)request.getSession().getAttribute("user");
       BeanUtils.copyProperties(question,questionDTO);
       questionDTO.setUser(user);
        return questionDTO;
    }

    public void insertOrUpdate(Question question) {
        if (question.getId()==null){
            //创建
            question.setGmtcreate(System.currentTimeMillis());
            question.setGmtmodified(question.getGmtcreate());
            //插入数据库
            questionMapper.insert(question);
        }else{
            //更新问题
            question.setGmtmodified(System.currentTimeMillis());

            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExample(question, example);
        }


    }
}
