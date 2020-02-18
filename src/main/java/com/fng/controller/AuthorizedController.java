package com.fng.controller;

import com.fng.dto.AccessTokenDTO;
import com.fng.dto.GitHubUser;
import com.fng.mapper.UserMapper;
import com.fng.provider.GitHunProvider;
import com.fng.service.UserService;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state, Model model,
                           HttpServletResponse response){
        AccessTokenDTO dto = new AccessTokenDTO();
        dto.setCode(code);
        dto.setRedirect_uri(gitHubDirectUri);
        dto.setState(state);
        dto.setClient_id(ClientId);
        dto.setClient_secret(ClientSecret);
        String accessToken = gitHunProvider.getAccessToken(dto);
        GitHubUser gitHubUser = gitHunProvider.getUser(accessToken);
        if (gitHubUser!=null&&gitHubUser.getId()!=null){
            //做登陆成功的操作
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setAvatar_url(gitHubUser.getAvatar_url());
            user.setBio(gitHubUser.getBio());
            //判断创建或是更新用户数据
            userService.createOrUpdate(user);
            //写cookie和session
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败
            //重新登陆
            return "redirect:/";
        }


    }
}
