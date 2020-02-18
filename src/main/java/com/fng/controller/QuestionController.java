package com.fng.controller;

import com.fng.dto.QuestionDTO;
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
    private QuestionService questionService;


    @GetMapping("/question/{id}")
    public String question(@PathVariable("id")Integer id,
                           Model model,
                           HttpServletRequest request){
      QuestionDTO questionDTO= questionService.getById(id,request);
      model.addAttribute("questionDTO",questionDTO);
      return "question";
    }
}
