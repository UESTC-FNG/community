package com.fng.controller;

import com.fng.dto.CommentDTO;
import com.fng.dto.QuestionDTO;
import com.fng.enums.CommentTypeEnums;
import com.fng.mapper.QuestionExtMapper;
import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.fng.model.User;
import com.fng.service.CommentService;
import com.fng.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class QuestionController {

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required =  false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionService questionService;
    @Autowired(required=false)
    private QuestionMapper questionMapper;
    @Autowired
    private CommentService commentService;


    @GetMapping("/question/{id}")
    public String question(@PathVariable("id")Long id,
                           Model model,
                           HttpServletRequest request){
        //累加阅读数
        questionService.incView(id);
        //这里的questionDTO，中的user是使用者  需要的姓名是作者的
        QuestionDTO questionDTO= questionService.getById(id,request,model);
        //查询作者姓名
        User creator = userMapper.selectByPrimaryKey(questionDTO.getCreator());
        model.addAttribute("questionDTO",questionDTO);
        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnums.QUESTION);
        model.addAttribute("commentDTOList",commentDTOList);
        return "question";
    }
}



