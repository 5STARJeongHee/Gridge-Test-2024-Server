package com.example.demo.src.user;

import com.example.demo.common.Constant.*;
import com.example.demo.src.token.repository.RefreshTokenRepository;
import com.example.demo.src.token.entity.RefreshToken;
import com.example.demo.src.user.model.GetSocialOAuthRes;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.src.user.model.SecurityUser;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;


    public GetSocialOAuthRes onOauthAuthenticationSuccess(Authentication authentication, String oauthId, SocialLoginType socialLoginType, boolean exist){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if(!exist){
            return GetSocialOAuthRes.builder()
                    .userId(user.getUsername())
                    .oauthId(oauthId)
                    .socialLoginType(socialLoginType)
                    .build();
        }

        // 인증 성공 이후 토큰 발급 및 반환 세팅
        // 리프레쉬 토큰 생성 및 저장
        String refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user.getUsername(), refreshToken);

        // 엑세스 토큰 생성
        String accessToken = jwtService.generateAccessToken(user);
        return GetSocialOAuthRes.builder()
                .userId(user.getUsername())
                .oauthId(oauthId)
                .socialLoginType(socialLoginType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public PostLoginRes onAuthenticationSuccess(Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        // 인증 성공 이후 토큰 발급 및 반환 세팅
        // 리프레쉬 토큰 생성 및 저장
        String refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user.getUsername(), refreshToken);

        // 엑세스 토큰 생성
        String accessToken = jwtService.generateAccessToken(user);

        return PostLoginRes.builder()
                .userId(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    private void saveRefreshToken(String userId, String newRefreshToken){
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }
}
