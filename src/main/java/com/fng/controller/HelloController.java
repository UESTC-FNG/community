package com.fng.controller;


import com.fng.dto.QuestionDTO;
import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.fng.service.QuestionService;
import com.model.Question;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HelloController {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        //questionMapper 针对 question,需要对question user进行组合，因此创建service层
        List<QuestionDTO> questionDTOList =questionService.list();
        model.addAttribute("questionDTOList",questionDTOList);
        return "index";
    }
}
