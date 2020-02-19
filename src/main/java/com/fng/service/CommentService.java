package com.fng.service;

import com.fng.enums.CommentTypeEnums;
import com.fng.exception.CustomizeErrorCode;
import com.fng.exception.CustomizeException;
import com.fng.mapper.CommentMapper;
import com.fng.mapper.QuestionExtMapper;
import com.fng.mapper.QuestionMapper;
import com.fng.model.Comment;
import com.fng.model.Question;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;
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
                record.setCommitCount(1);
                questionExtMapper.incCommentCount(record);
            }
        }
    }


}
