package com.fng.service;

import com.fng.dto.CommentDTO;
import com.fng.enums.CommentTypeEnums;
import com.fng.enums.NotificationEnums;
import com.fng.enums.NotificationStatusEnums;
import com.fng.exception.CustomizeErrorCode;
import com.fng.exception.CustomizeException;
import com.fng.mapper.*;
import com.fng.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired(required = false)
    private NotificationMapper notificationMapper;
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
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //创建通知
            Notification notification = createNotification(comment, dbComment);
            notificationMapper.insert(notification);
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
                Notification questionNotification = createQuestionNotification(comment, question);
                notificationMapper.insert(questionNotification);

            }
        }
    }

    private Notification createQuestionNotification(Comment comment, Question question) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier( comment.getCommentator());
        notification.setReceiver(question.getCreator());
        notification.setType(NotificationEnums.REPLY_QUESTION.getType());
        notification.setStatus(NotificationStatusEnums.UNREAD.getType());
        notification.setOuterid(comment.getParentId());
        return notification;
    }

    private Notification createNotification(Comment comment, Comment dbComment) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier( comment.getCommentator());
        notification.setReceiver(dbComment.getCommentator());
        notification.setType(NotificationEnums.REPLY_COMMENT.getType());
        notification.setStatus(NotificationStatusEnums.UNREAD.getType());
        ;
        notification.setOuterid(comment.getParentId());
        return notification;
    }


    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnums type) {
        //查找comment
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
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
