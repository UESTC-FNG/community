package com.fng.controller;


import com.fng.dto.PageDTO;
import com.fng.mapper.UserMapper;
import com.fng.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "7")Integer size) {

            //questionMapper 针对 question,需要对question user进行组合，因此创建service层
        PageDTO pageNation =questionService.list(page,size);
        model.addAttribute("pageNation",pageNation);
        return "index";
    }
}
