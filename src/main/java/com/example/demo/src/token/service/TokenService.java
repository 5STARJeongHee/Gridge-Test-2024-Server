package com.example.demo.src.token.service;

import com.example.demo.common.config.JwtProperties;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.model.SecurityUser;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.example.demo.common.response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    private final JwtProperties jwtProperties;

    public String createNewAccessToken(String refreshToken) throws BaseException{
        if(!jwtService.validJwt(refreshToken)){
            throw new BaseException(INVALID_JWT);
        }

        String userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(userId);
        return jwtService.generateAccessToken(user);
    }

}
