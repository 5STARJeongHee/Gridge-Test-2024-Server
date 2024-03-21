package com.example.demo.src.token.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.token.repository.RefreshTokenRepository;
import com.example.demo.src.token.model.GetRefreshTokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_REFRESH_TOKEN;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public GetRefreshTokenRes findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(GetRefreshTokenRes::new)
                .orElseThrow(() -> new BaseException(NOT_FIND_REFRESH_TOKEN));
    }
}
