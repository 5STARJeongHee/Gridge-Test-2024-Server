package com.example.demo.src.token;

import com.example.demo.common.config.JwtProperties;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;

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
        UserDetails user = userDetailsService.loadUserByUsername(userId);
        return jwtService.generateAccessToken(user);
    }

}
