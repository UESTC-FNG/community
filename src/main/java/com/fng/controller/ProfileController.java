package com.fng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;

@Controller
public class ProfileController {

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          Model model){
        if ("questions".equals(action)){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","我的问题");
        }else if ("replies".equals(action)){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
