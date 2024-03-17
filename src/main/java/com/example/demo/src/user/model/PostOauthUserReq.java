package com.example.demo.src.user.model;

import com.example.demo.common.Constant.*;
import com.example.demo.src.user.entity.SocialUser;
import com.example.demo.src.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PostOauthUserReq {
    private String oauthId;
    private String userId;
    private SocialLoginType socialLoginType;
    private boolean exist;

    private PostUserReq user;

    public SocialUser toEntity(){
        return SocialUser.builder()
                .oauthId(this.oauthId)
                .oauthType(this.socialLoginType)
                .user(User.builder().email(userId).build())
                .build();
    }

    @Builder
    public PostOauthUserReq(String oauthId, String userId, SocialLoginType socialLoginType, boolean exist, PostUserReq postUserReq) {
        this.oauthId = oauthId;
        this.userId = userId;
        this.socialLoginType = socialLoginType;
        this.exist = exist;
        this.user = postUserReq;
    }
}
