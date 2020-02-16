package com.fng.controller;

import com.fng.dto.AccessTokenDTO;
import com.fng.dto.GitHubUser;
import com.fng.provider.GitHunProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthorizedController {

    @Autowired
    private GitHunProvider gitHunProvider;

    @Value("${github.client.id}")
    private String ClientId;

    @Value("${github.client.secret}")
    private String ClientSecret;

    @Value("${github.redirect.uri}")
    private String gitHubDirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state, Model model,
                           HttpServletRequest request){
        AccessTokenDTO dto = new AccessTokenDTO();
        dto.setCode(code);
        dto.setRedirect_uri(gitHubDirectUri);
        dto.setState(state);
        dto.setClient_id(ClientId);
        dto.setClient_secret(ClientSecret);
        String accessToken = gitHunProvider.getAccessToken(dto);
        GitHubUser user = gitHunProvider.getUser(accessToken);
        if (user!=null){
            //做登陆成功的操作
            //写cookie和session
            System.out.println("user有对象");
            HttpSession session = request.getSession();
            session.setAttribute("user",user);//将user对象放入session
            return "redirect:/index";

        }else{
            //登录失败
            //重新登陆
            System.out.println("user无对象");

        }
        return "index";


    }
}
