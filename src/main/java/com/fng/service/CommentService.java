package com.fng.service;

import com.fng.dto.CommentCreateDTO;
import com.fng.dto.CommentDTO;
import com.fng.enums.CommentTypeEnums;
import com.fng.exception.CustomizeErrorCode;
import com.fng.exception.CustomizeException;
import com.fng.mapper.CommentMapper;
import com.fng.mapper.QuestionExtMapper;
import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.fng.model.Comment;
import com.fng.model.CommentExample;
import com.fng.model.Question;
import com.fng.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {
    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required= false)
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
  //判断parentId == null
        if (comment.getParentId()==null||comment.getParentId()==0) {
           throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()==null|| !CommentTypeEnums.isExit(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_WRONG);
        };

        if (comment.getType()== CommentTypeEnums.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question== null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }else{
                commentMapper.insert(comment);
                Question record = new Question();
                record.setId(comment.getParentId());
                record.setCommentCount(1);
                questionExtMapper.incCommentCount(record);
            }
        }
    }


    public List<CommentDTO> listByQuestionId(Long id) {
        //查找comment
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(CommentTypeEnums.QUESTION.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        List<CommentDTO> list=new ArrayList<>();
        for (Comment comment:comments){
            CommentDTO commentDTO=new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            //根据评论者id查询评论者
            User user = userMapper.selectByPrimaryKey(comment.getCommentator());
            commentDTO.setUser(user);
            list.add(commentDTO);
        }

        return list;
    }
}
