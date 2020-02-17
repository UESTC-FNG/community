package com.fng.controller;

import com.fng.mapper.QuestionMapper;
import com.fng.mapper.UserMapper;
import com.model.Question;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    private Question question;

    @Autowired
    private QuestionMapper questionMapper;
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value="title",required = false) String title,
            @RequestParam("description") String description,
            @RequestParam("tag")String tag,
                HttpServletRequest request,
            Model model
    ){
        //将获取的信息填入文本框，防止信息丢失
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        //判断输入信息是否为空
        if (title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description==null||description==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        User user=null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
                System.out.println("找到了user");
                System.out.println(user);
                break;
            }
        }
        if (user==null){
            model.addAttribute("error","用户未登录");

            return "publish";
        }
        //创建Question对象
        Question question=new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtcreate(System.currentTimeMillis());
        question.setGmtmodified(question.getGmtcreate());

        questionMapper.insertQuestion(question);
        System.out.println("用户已登录");

        return "redirect:/";
    }
}
