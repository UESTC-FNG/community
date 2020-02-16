package com.fng.provider;


import com.alibaba.fastjson.JSON;
import com.fng.dto.AccessTokenDTO;
import com.fng.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 */
@Component
public class GitHunProvider {
    public String getAccessToken(AccessTokenDTO dto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(dto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();

            //分割返回的access_token和token_type
            String[] tokenAndToken_type=string.split("&");
            String access_token=tokenAndToken_type[0];
            String token_type=tokenAndToken_type[1];
            String accessTaken=  access_token.split("=")[1];
            String tokenType=token_type.split("=")[1];
            return accessTaken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            System.out.println(gitHubUser.getName());
            return gitHubUser;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    }




