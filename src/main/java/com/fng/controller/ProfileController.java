package com.fng.controller;

import com.fng.dto.NotificationDTO;
import com.fng.dto.PageDTO;
import com.fng.mapper.UserMapper;
import com.fng.service.NotificationService;
import com.fng.service.QuestionService;
import com.fng.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired(required = false)
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name="size",defaultValue = "5")Integer size){


        //是否登录的判断
       User user = (User) request.getSession().getAttribute("user");

        if (user==null){
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","我的问题");
            PageDTO pageDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pageNation",pageDTO);
            int count = notificationService.unreadCount(user);
            model.addAttribute("unreadCount",count);
        }else if ("replies".equals(action)){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","最新回复");
            //需要查询当前用户是否有新通知
            //获取用户id
            Integer userId = user.getId();
            List<NotificationDTO> notificationDTOList = notificationService.listByUserId(userId, model);
            //添加通知的信息
            model.addAttribute("notificationDTOList",notificationDTOList);
        }

        return "profile";
    }
}
