package com.fng.controller;

import com.fng.dto.QuestionDTO;
import com.fng.mapper.QuestionExtMapper;
import com.fng.mapper.QuestionMapper;
import com.fng.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class QuestionController {

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required = false)
    private QuestionService questionService;
    @Autowired(required=false)
    private QuestionMapper questionMapper;


    @GetMapping("/question/{id}")
    public String question(@PathVariable("id")Long id,
                           Model model,
                           HttpServletRequest request){
        //累加阅读数
        questionService.incView(id);
        QuestionDTO questionDTO= questionService.getById(id,request);
        model.addAttribute("questionDTO",questionDTO);
        return "question";
    }
}



