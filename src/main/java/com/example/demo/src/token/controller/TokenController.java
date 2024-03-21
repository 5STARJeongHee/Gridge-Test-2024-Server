package com.example.demo.src.token.controller;

import com.example.demo.src.token.service.TokenService;
import com.example.demo.src.token.model.GetAccessTokenRes;
import com.example.demo.src.token.model.PostAccessTokenReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰", description = "JWT 액세스 토큰 발급 api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/token")
public class TokenController {
    private final TokenService tokenService;

    @Operation(summary = "액세스 토큰 재발급 Api", description = "리프레시 토큰을 통해 액세스 토큰을 재발급 받는다.")
    @PostMapping("")
    public ResponseEntity<GetAccessTokenRes> createNewAccessToken(@RequestBody PostAccessTokenReq request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GetAccessTokenRes(newAccessToken));
    }
}
