package com.fng.service;


import com.fng.dto.PageDTO;
import com.fng.dto.QuestionDTO;
import com.fng.dto.QuestionQueryDTO;
import com.fng.exception.CustomizeErrorCode;
import com.fng.exception.CustomizeException;
import com.fng.mapper.QuestionExtMapper;
import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.fng.model.Question;
import com.fng.model.QuestionExample;
import com.fng.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired(required=false)
    private UserMapper userMapper;

    @Autowired(required=false)
    private QuestionMapper questionMapper;

    @Autowired(required=false)
    private QuestionExtMapper questionExtMapper;



    public PageDTO list(String search,Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)){
            String[] strings = StringUtils.split(search, " ");
            search = String.join("|", strings);
        }
        if (page<1){
            page=1;
        }
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount =questionExtMapper.countBySearch(questionQueryDTO);
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
        example.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
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

    public QuestionDTO getById(Long id,
                               HttpServletRequest request,
                               Model model) {
        //初始化QuestionDTO
        QuestionDTO questionDTO=new QuestionDTO();
        //先调用questionMapper查询question
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //加入user
        User user = (User)request.getSession().getAttribute("user");
        model.addAttribute("user",user);
       BeanUtils.copyProperties(question,questionDTO);
       //查找作者
        User creator = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(creator);
        return questionDTO;
    }

    public void insertOrUpdate(Question question) {
        if (question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            //插入数据库
            questionMapper.insert(question);
        }else{
            //更新问题
            question.setGmtModified(System.currentTimeMillis());

            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExample(question, example);
            if (updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }


    }

    public void incView(Long id) {
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExtMapper.incView(record);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",|，");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setTag(regexpTag);
        question.setId(queryDTO.getId());
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOList = questions.stream().map(q -> {
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return  questionDTO;
        }).collect(Collectors.toList());
        return questionDTOList;
    }
}
