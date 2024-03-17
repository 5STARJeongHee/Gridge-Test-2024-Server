package com.example.demo.src.token.model;

import com.example.demo.src.token.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetRefreshTokenRes {
    private Long id;
    private String userId;
    private String refreshToken;

    public GetRefreshTokenRes(RefreshToken token) {
        this.id = token.getId();
        this.userId = token.getUserId();
        this.refreshToken = token.getRefreshToken();
    }
}
