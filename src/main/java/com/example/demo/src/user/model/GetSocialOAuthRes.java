package com.example.demo.src.user.model;

import com.example.demo.common.Constant.*;
import com.example.demo.src.user.entity.SocialUser;
import lombok.*;

//클라이언트로 보낼 jwtToken, accessToken등이 담긴 객체
@Getter
@Setter
@NoArgsConstructor
public class GetSocialOAuthRes {

    private String userId;
    private String oauthId;
    private SocialLoginType socialLoginType;
    private String accessToken;
    private String refreshToken;

    private boolean serviceTermsCheck;

    @Builder
    public GetSocialOAuthRes(String userId, String oauthId, SocialLoginType socialLoginType, String accessToken, String refreshToken, boolean serviceTermsCheck) {
        this.userId = userId;
        this.oauthId = oauthId;
        this.socialLoginType = socialLoginType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.serviceTermsCheck = serviceTermsCheck;
    }

    public GetSocialOAuthRes(SocialUser socialUser){
        this.oauthId = socialUser.getOauthId();
        this.socialLoginType = socialUser.getOauthType();
    }
}
