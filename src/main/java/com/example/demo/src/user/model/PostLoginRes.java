package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PostLoginRes {

    private String userId;
    private String accessToken;
    private String refreshToken;
    private boolean serviceTermsCheck;

    @Builder
    public PostLoginRes(String userId, String accessToken, String refreshToken, boolean serviceTermsCheck) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.serviceTermsCheck = serviceTermsCheck;
    }
}
