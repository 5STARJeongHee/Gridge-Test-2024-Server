package com.example.demo.src.token;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.token.entity.RefreshToken;
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
