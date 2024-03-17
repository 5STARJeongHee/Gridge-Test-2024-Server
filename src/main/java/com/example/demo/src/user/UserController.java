package com.example.demo.src.user;


import com.example.demo.common.Constant;
import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.PasswordValidStatus;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
public class UserController {


    private final UserService userService;
    private final OauthUserService oauthUserService;
    private final OAuthService oAuthService;


    /**
     * 로그인 API
     * [POST] /app/users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
        // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
        if(postLoginReq.getUserId() == null || postLoginReq.getUserId().equals("")){
            return new BaseResponse<>(USERS_EMPTY_ID);
        }
        if(postLoginReq.getPassword() == null || postLoginReq.getPassword().equals("")){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }

    /**
     * email 중복 체크 API
     *
     */
    @GetMapping("/duplicate/email")
    public BaseResponse<GetUserRes> checkEmail(@RequestParam("email") String email){
        if(email == null || email.equals("")){
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        if(ValidationRegex.isRegexEmail(email)){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        GetUserRes user = userService.getUserByEmail(email);
        return new BaseResponse<>(user);
    }

    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getEmail() != null && !postUserReq.getEmail().equals("")){
            if(!isRegexEmail(postUserReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }
        }
        if(postUserReq.getPhoneNumber() != null && !postUserReq.getPhoneNumber().equals("")){
            if(!isRegexPhone(postUserReq.getPhoneNumber())){
                return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
            }
        }

        if(postUserReq.getName() == null || postUserReq.getNickname().equals("")){
            return new BaseResponse<>(USERS_EMPTY_NAME);
        }
        if(postUserReq.getNickname() == null || postUserReq.getNickname().equals("")){
            return new BaseResponse<>(USERS_EMPTY_USER_NICKNAME);
        }
        if(postUserReq.getPassword() == null || postUserReq.getPassword().equals("")){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }
        PasswordValidStatus passwordValidStatus = isPasswordValid(postUserReq.getPassword());
        if(!passwordValidStatus.equals(PasswordValidStatus.VALID)){
            switch (passwordValidStatus){
                case FORMAT_NOT_VALID:
                    return new BaseResponse<>(POST_USERS_INVALID_PASSOWRD_FORMAT);
                case CONTINUOS:
                    return new BaseResponse<>(POST_USERS_INVALID_PASSOWRD_CONTINUOS);
                case DUPLICATE:
                    return new BaseResponse<>(POST_USERS_INVALID_PASSOWRD_DUPLICATE);
            }
        }

        if(!postUserReq.isServicePolicyAgreed() || !postUserReq.isDataPolicyAgreed() || !postUserReq.isLocationPolicyAgreed()){
            return new BaseResponse<>(USERS_EMPTY_CONSENT_AGREED);
        }

        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }
    // Oauth 용 회원 가입
    @ResponseBody
    @PostMapping("/oauth")
    public BaseResponse<PostOauthUserRes> createOauthUser(@RequestBody PostOauthUserReq postOauthUserReq) {
        if(postOauthUserReq.getUser() != null && postOauthUserReq.getUser().getId() == null){
            PostUserReq postUserReq = postOauthUserReq.getUser();

            if(postUserReq.getEmail() != null && !postUserReq.getEmail().equals("")){
                if(!isRegexEmail(postUserReq.getEmail())){
                    return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
                }
            }
            if(postUserReq.getPhoneNumber() != null && !postUserReq.getPhoneNumber().equals("")){
                if(!isRegexPhone(postUserReq.getPhoneNumber())){
                    return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
                }
            }

            if(postUserReq.getNickname() == null || postUserReq.getNickname().equals("")){
                return new BaseResponse<>(USERS_EMPTY_NAME);
            }
            if(postUserReq.getNickname() == null || postUserReq.getNickname().equals("")){
                return new BaseResponse<>(USERS_EMPTY_USER_NICKNAME);
            }
            if(postUserReq.getPassword() == null || postUserReq.getPassword().equals("")){
                return new BaseResponse<>(USERS_EMPTY_PASSWORD);
            }
            PasswordValidStatus passwordValidStatus = isPasswordValid(postUserReq.getPassword());
            if(!passwordValidStatus.equals(PasswordValidStatus.VALID)){
                switch (passwordValidStatus){
                    case FORMAT_NOT_VALID:
                        return new BaseResponse<>(POST_USERS_INVALID_PASSOWRD_FORMAT);
                    case CONTINUOS:
                        return new BaseResponse<>(POST_USERS_INVALID_PASSOWRD_CONTINUOS);
                    case DUPLICATE:
                        return new BaseResponse<>(POST_USERS_INVALID_PASSOWRD_DUPLICATE);
                }
            }

            if(!postUserReq.isServicePolicyAgreed() || !postUserReq.isDataPolicyAgreed() || !postUserReq.isLocationPolicyAgreed()){
                return new BaseResponse<>(USERS_EMPTY_CONSENT_AGREED);
            }
        }

        PostOauthUserRes postOauthUserRes = oauthUserService.createOAuthUser(postOauthUserReq);
        return new BaseResponse<>(postOauthUserRes);
    }

    /**
     * 회원 1명 조회 API
     * [GET] /app/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") Long userId) {
        GetUserRes getUserRes = userService.getUser(userId);
        return new BaseResponse<>(getUserRes);
    }



    /**
     * 유저정보변경 API
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUser(@PathVariable("userId") Long userId, @RequestBody PatchUserReq patchUserReq){
        if(patchUserReq.getName() == null || patchUserReq.getName().equals("")){
            throw new BaseException(USERS_EMPTY_NAME);
        }
        if(patchUserReq.getNickname() == null || patchUserReq.getNickname().equals("")){
            throw new BaseException(USERS_EMPTY_NICKNAME);
        }
        if(patchUserReq.getBirthDay() == null){
            throw new BaseException(USERS_EMPTY_BIRTHDAY);
        }
        if(patchUserReq.getPhoneNumber() == null || patchUserReq.getPhoneNumber().equals("")){
            throw new BaseException(USERS_EMPTY_PHONE_NUMBER);
        }
        if(!ValidationRegex.isRegexPhone(patchUserReq.getPhoneNumber())){
            throw new BaseException(POST_USERS_INVALID_PHONE_NUMBER);
        }
        userService.modifyProfile(userId, patchUserReq);

        String result = "수정 완료!!";
        return new BaseResponse<>(result);

    }

    /**
     * 유저정보삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{userId}")
    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId){

        userService.deleteUser(userId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    @ResponseBody
    @PatchMapping("/{userId}/service-terms")
    public BaseResponse<PatchServiceTermsRes> updateServiceTerms(@PathVariable("userId") Long userId, @RequestBody PatchServiceTermsReq patchServiceTermsReq){
        if(!patchServiceTermsReq.isServicePolicyAgreed() || !patchServiceTermsReq.isDataPolicyAgreed() || !patchServiceTermsReq.isLocationPolicyAgreed()){
            return new BaseResponse<>(USERS_EMPTY_CONSENT_AGREED);
        }
        PatchServiceTermsRes patchServiceTermsRes = userService.updateServiceTerms(patchServiceTermsReq);
        return new BaseResponse<>(patchServiceTermsRes);
    }

    @ResponseBody
    @PatchMapping("/{userId}/lock")
    public BaseResponse<String> lockUser(@PathVariable("userId") Long userId){
        userService.lockUser(userId);
        String result = "계정 잠금 완료";
        return new BaseResponse<>(result);
    }

    /**
     * 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url
     * [GET] /app/users/auth/:socialLoginType/login
     * @return void
     */
    @GetMapping("/auth/{socialLoginType}/login")
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.accessRequest(socialLoginType);
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */
    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/login/callback")
    public BaseResponse<GetSocialOAuthRes> socialLoginCallback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException, BaseException {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code : {}", code);
        Constant.SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }

}
