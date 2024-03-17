package com.example.demo.common.oauth;

import com.example.demo.common.SocialLoginParam;
import com.example.demo.src.user.model.KakaoOAuthToken;
import com.example.demo.src.user.model.KakaoUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KaKaoOauth implements SocialOauth{

    @Value("${spring.OAuth2.kakao.url}")
    private String KAKAO_SNS_URL;

    @Value("${spring.OAuth2.kakao.client-id}")
    private String KAKAO_SNS_CLIENT_ID;

    //@Value("${spring.OAuth2.kakao.client-secret}")
    //private String KAKAO_SNS_CLIENT_SECRET;

    @Value("${spring.OAuth2.kakao.callback-login-url}")
    private String KAKAO_SNS_CALLBACK_LOGIN_URL;

    @Value("${spring.OAuth2.kakao.scope}")
    private String KAKAO_DATA_ACCESS_SCOPE;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;
    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put(SocialLoginParam.RESPONSE_TYPE.toString(), "code");
        params.put(SocialLoginParam.CLIENT_ID.toString(), KAKAO_SNS_CLIENT_ID);
        params.put(SocialLoginParam.REDIRECT_URI.toString(), KAKAO_SNS_CALLBACK_LOGIN_URL);
        params.put(SocialLoginParam.SCOPE.toString(), KAKAO_DATA_ACCESS_SCOPE);
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = KAKAO_SNS_URL + "?" + parameterString;
        log.info("redirectURL = ", redirectURL);
        return redirectURL;
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(SocialLoginParam.CODE.toString(), code);
        params.add(SocialLoginParam.CLIENT_ID.toString(), KAKAO_SNS_CLIENT_ID);
//        params.put(SocialLoginParam.CLIENT_SECRET.toString(), KAKAO_SNS_CLIENT_SECRET);
        params.add(SocialLoginParam.REDIRECT_URI.toString(), KAKAO_SNS_CALLBACK_LOGIN_URL);
        params.add(SocialLoginParam.GRANT_TYPE.toString(), "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity=restTemplate.postForEntity(KAKAO_TOKEN_REQUEST_URL,
                request,String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity;
        }
        return null;
    }

    public KakaoOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() = {}", response.getBody());
        KakaoOAuthToken kakaoOAuthToken = objectMapper.readValue(response.getBody(), KakaoOAuthToken.class);
        return kakaoOAuthToken;
    }

    public ResponseEntity<String> requestUserInfo(KakaoOAuthToken oAuthToken){
        String KAKAO_USERINFO_REQUEST_URL="https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);

        log.info("response.getBody() = {}", response.getBody());

        return response;

    }

    public KakaoUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException{
        KakaoUser kakaoUser = objectMapper.readValue(userInfoRes.getBody(), KakaoUser.class);
        return kakaoUser;
    }

}
