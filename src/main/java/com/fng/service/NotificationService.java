package com.fng.service;

import com.fng.dto.NotificationDTO;
import com.fng.enums.CommentTypeEnums;
import com.fng.enums.NotificationEnums;
import com.fng.enums.NotificationStatusEnums;
import com.fng.mapper.*;
import com.fng.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class NotificationService {

    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    @Autowired(required =  false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private NotificationExtMapper notificationExtMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;



    public  void insert(Comment comment) {
        Notification notification=new Notification();
        notification.setType(comment.getType());
        notification.setStatus(0);
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(comment.getCommentator());
        if (comment.getType()==1){
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            notification.setReceiver(question.getCreator());
        }else{
            Comment comment1 = commentMapper.selectByPrimaryKey(comment.getParentId());
            notification.setReceiver(comment1.getCommentator());
        }
        notification.setOuterid(comment.getId());
        notificationMapper.insert(notification);
    }

    public List<NotificationDTO> listByUserId(Integer userId, Model model) {
        //查出type为0的和type为1的通知，（状态为0 未读）
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        List<Notification> notifications = notificationMapper.selectByExample(example);
        //根据发送者id，获取发送者信息
        Integer unreadCount=0;
        List<NotificationDTO> notificationDTOList=new ArrayList<>();
        for (Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            Integer notifierId = notification.getNotifier();
            User notifier = userMapper.selectByPrimaryKey(notifierId);
            notificationDTO.setNotifier(notifier);
            notificationDTO.setId(notification.getId());
            notificationDTO.setGmtCreate(notification.getGmtCreate());
            notificationDTO.setStatus(notification.getStatus());
            //根据回复的类型，设置不同的文案
            Long outerid = notification.getOuterid();
            Comment comment = commentMapper.selectByPrimaryKey(outerid);
            if (comment.getType()== CommentTypeEnums.QUESTION.getType()){
                Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
                notificationDTO.setOuterTitle(question.getTitle());
                notificationDTO.setType(NotificationEnums.REPLY_QUESTION.getName());
                notificationDTO.setParentId(comment.getParentId());
            }else{
                //从comment中查找
                Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
                notificationDTO.setOuterTitle(parentComment.getContent());
                notificationDTO.setType(NotificationEnums.REPLY_COMMENT.getName());
                notificationDTO.setParentId(parentComment.getParentId());
            }
            if (notification.getStatus()== NotificationStatusEnums.UNREAD.getType()){
                unreadCount++;
            }
            notificationDTOList.add(notificationDTO);
        }
        //添加未读数
        model.addAttribute("unreadCount",unreadCount);

        //sort默认为升序排序  让其按照通知时间，降序排列
        Collections.sort(notificationDTOList, new Comparator<NotificationDTO>() {
            @Override
            public int compare(NotificationDTO o1, NotificationDTO o2) {
                if (o1.getGmtCreate()>o2.getGmtCreate()){
                    return -1;
                }else if (o1.getGmtCreate()<o2.getGmtCreate()){
                    return 1;
                }
                return 0;
            }
        });
        return notificationDTOList;

    }

    public int unreadCount(User user) {
        //根据id查询通知数量
        int count = notificationExtMapper.unreadCountById(user);
        return count;

    }

    public Long getParentIdById(Long id) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterid());
        if (comment.getType()==1){
            return comment.getParentId();
        }else{
            Comment comment1 = commentMapper.selectByPrimaryKey(comment.getParentId());
            return  comment1.getParentId();
        }
    }
}
