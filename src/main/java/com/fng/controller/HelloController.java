package com.fng.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name") String name, Model model){
        //将浏览器传递的数据，放入model
        model.addAttribute("name",name);

        return "hello";//去模板目录查找hello.html
    }
}
