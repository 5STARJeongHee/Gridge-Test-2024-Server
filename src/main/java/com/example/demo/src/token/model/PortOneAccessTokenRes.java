package com.example.demo.src.token.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortOneAccessTokenRes {
    private int code;
    private String message;
    private Response response;
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String access_token;
        private long now;
        private long expired_at;
    }
}
