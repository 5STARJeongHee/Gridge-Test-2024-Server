package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.SocialUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOauthUserRes {
    private String oauthId;
    private String userId;

    public PostOauthUserRes(SocialUser user){
        this.oauthId = user.getOauthId();
        this.userId = user.getUser().getEmail();
    }
}
