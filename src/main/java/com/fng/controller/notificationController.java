package com.fng.controller;

import com.fng.mapper.NotificationMapper;
import com.fng.model.Notification;
import com.fng.model.NotificationExample;
import com.fng.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller()
public class notificationController {

    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationService notificationService;
    @RequestMapping("/replies/{action}")
    public String notification(@PathVariable(name="action")String action,
                               HttpServletRequest request){
        //进行已读操作
        Notification notification = notificationMapper.selectByPrimaryKey(Long.valueOf(action));
        notification.setStatus(1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andIdEqualTo(Long.valueOf(action));
        notificationMapper.updateByExample(notification,example);
        Long parentIdById = notificationService.getParentIdById(Long.valueOf(action));

        //重定向
        return "redirect:/question/"+parentIdById;


    }



}
