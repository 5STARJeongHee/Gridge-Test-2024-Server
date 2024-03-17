package com.example.demo.src.token;

import com.example.demo.src.token.model.GetAccessTokenRes;
import com.example.demo.src.token.model.PostAccessTokenReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/token")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("")
    public ResponseEntity<GetAccessTokenRes> createNewAccessToken(@RequestBody PostAccessTokenReq request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GetAccessTokenRes(newAccessToken));
    }
}
