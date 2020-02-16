package com.fng.controller;

import com.fng.dto.AccessTokenDTO;
import com.fng.provider.GitHunProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                           @RequestParam(name="state")String state, Model model){
        AccessTokenDTO dto = new AccessTokenDTO();
        dto.setCode(code);
        dto.setRedirect_uri(gitHubDirectUri);
        dto.setState(state);
        dto.setClient_id(ClientId);
        dto.setClient_secret(ClientSecret);
        String accessToken = gitHunProvider.getAccessToken(dto);
        gitHunProvider.getUser(accessToken);

        return "index";
    }
}
