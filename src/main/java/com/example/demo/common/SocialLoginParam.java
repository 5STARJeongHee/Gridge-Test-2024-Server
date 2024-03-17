package com.example.demo.common;

import lombok.Getter;

@Getter
public enum SocialLoginParam {
    SCOPE("scope"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    REDIRECT_URI("redirect_uri"),
    RESPONSE_TYPE("response_type"),
    GRANT_TYPE("grant_type"),
    CODE("code"),
    ;

    private String parameter;

    SocialLoginParam(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return parameter;
    }
}
