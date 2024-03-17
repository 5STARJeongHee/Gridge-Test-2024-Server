package com.example.demo.common.oauth;

import com.example.demo.common.Constant.*;
import com.example.demo.common.Role;
import com.example.demo.common.config.JwtProperties;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.token.RefreshTokenRepository;
import com.example.demo.src.token.RefreshTokenService;
import com.example.demo.src.token.entity.RefreshToken;
import com.example.demo.src.user.CustomAuthenticationSuccessHandler;
import com.example.demo.src.user.OauthUserService;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.entity.SocialUser;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

import static com.example.demo.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final KaKaoOauth kaKaoOauth;
    private final HttpServletResponse response;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final OauthUserService oAuthUserService;

    private final CustomAuthenticationSuccessHandler successHandler;
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    // OAuth2ClientRe
    public void accessRequest(SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType){ //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
            case GOOGLE:{
                redirectURL= googleOauth.getOauthRedirectURL();
                break;
            }
            case KAKAO:{
                redirectURL= kaKaoOauth.getOauthRedirectURL();
                break;
            }
            default:{
                throw new BaseException(INVALID_OAUTH_TYPE);
            }

        }

        response.sendRedirect(redirectURL);
    }


    public GetSocialOAuthRes oAuthLoginOrJoin(SocialLoginType socialLoginType, String code) throws IOException {

        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);

                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                String oauthId = googleUser.getId();
                String email = googleUser.getEmail();
                //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
                return attemptAuthentication(oauthId, email, SocialLoginType.GOOGLE);
            }

            case KAKAO: {
                ResponseEntity<String> accessTokenResponse = kaKaoOauth.requestAccessToken(code);

                KakaoOAuthToken oAuthToken = kaKaoOauth.getAccessToken(accessTokenResponse);

                ResponseEntity<String> userInfoResponse = kaKaoOauth.requestUserInfo(oAuthToken);

                KakaoUser kakaoUser = kaKaoOauth.getUserInfo(userInfoResponse);
                String oauthId = kakaoUser.getId();
                String email = kakaoUser.getKakao_account().email;
                return attemptAuthentication(oauthId, email, SocialLoginType.KAKAO);
            }
            default: {
                throw new BaseException(INVALID_OAUTH_TYPE);
            }

        }
    }

    private GetSocialOAuthRes attemptAuthentication(String oauthId, String email, SocialLoginType socialLoginType) {
        SecurityUser user = null;
        GetSocialOAuthRes socialUser = null;
        try{
            // 유저는 존재하나 Oauth 유저 등록이 안됬거나 다른 종류의 Oauth user가 등록될 경우
            user = (SecurityUser) userDetailsService.loadUserByUsername(email);
            if(!oAuthUserService.checkSocialUserByOauthId(oauthId, socialLoginType)){
                oAuthUserService.createOAuthUser(PostOauthUserReq.builder()
                        .oauthId(oauthId)
                        .userId(email)
                        .socialLoginType(socialLoginType)
                        .exist(false).build()
                );
            }

            // 유저 상태 처리
            if(!user.isEnabled()){
                throw new BaseException(INACTIVE_USER);
            }
            if(!user.isAccountNonLocked()){
                throw new BaseException(LOCK_USER);
            }
            if(!user.isAccountNonExpired()){
                throw new BaseException(EXPIRE_USER);
            }

        }catch (UsernameNotFoundException | BaseException e){
            // 기존 등록된 유저가 존재 하지 않아 회원가입을 진행하면서 Oauth 유저 등록이 필요한 경우
            user = SecurityUser.builder().email(email).role(Role.GUEST).build();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    "",
                    user.getAuthorities()
            );
            return successHandler.onOauthAuthenticationSuccess(authentication, oauthId, socialLoginType, false);
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                "",
                user.getAuthorities()
        );
        GetSocialOAuthRes getSocialOAuthRes = successHandler.onOauthAuthenticationSuccess(authentication, oauthId, socialLoginType, true);
        getSocialOAuthRes.setServiceTermsCheck(user.isServicePolicyAgreed() && user.isDataPolicyAgreed() && user.isLocationPolicyAgreed());
        return getSocialOAuthRes;
    }

}
