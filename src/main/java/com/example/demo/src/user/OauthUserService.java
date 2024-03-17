package com.example.demo.src.user;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.entity.SocialUser;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.GetSocialOAuthRes;
import com.example.demo.src.user.model.PostOauthUserReq;
import com.example.demo.src.user.model.PostOauthUserRes;
import com.example.demo.src.user.model.PostUserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class OauthUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final SocialUserRepository socialUserRepository;

    public PostOauthUserRes createOAuthUser(PostOauthUserReq postOauthUserReq) throws BaseException{
        User saveUser = null;
        SocialUser socialUser = postOauthUserReq.toEntity();
        User user = userRepository.findByEmail(postOauthUserReq.getUserId())
                .orElse(null)
                ;

        if(user != null){
            socialUser.updateUser(user);
        } else {
            PostUserReq postUserReq = postOauthUserReq.getUser();
            Optional<User> checkUser = null;
            if(postUserReq.getEmail() != null){
                checkUser = userRepository.findByEmailAndState( postUserReq.getEmail(), ACTIVE);
                if(checkUser.isPresent()){
                    throw new BaseException(POST_USERS_EXISTS_EMAIL);
                }
                postUserReq.setEmail(postUserReq.getEmail());
            }

            if(postUserReq.getPhoneNumber() != null){
                checkUser = userRepository.findByPhoneNumberAndState(postUserReq.getPhoneNumber(), ACTIVE);
                if(checkUser.isPresent()){
                    throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
                }
                postUserReq.setPhoneNumber(postUserReq.getPhoneNumber());
            }

            checkUser = userRepository.findByNicknameAndState(postUserReq.getNickname(), ACTIVE);
            if(checkUser.isPresent()){
                throw new BaseException(POST_USERS_EXISTS_USER_NICKNAME);
            }

            String encryptPwd;
            try {
                encryptPwd = passwordEncoder.encode(postUserReq.getPassword());
                postUserReq.setPassword(encryptPwd);

            } catch (Exception exception) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }
            saveUser = userRepository.save(postUserReq.toEntity());
            socialUser.updateUser(saveUser);
        }

        SocialUser result = socialUserRepository.save(socialUser);
        return new PostOauthUserRes(result);
    }

    @Transactional(readOnly = true)
    public GetSocialOAuthRes getSocialUserByEmail(String email, Constant.SocialLoginType socialLoginType){
        SocialUser socialUser = socialUserRepository.findByUserAndOauthType(
                        User.builder().email(email).build(),
                        socialLoginType)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER))
                ;
        GetSocialOAuthRes socialOAuthRes = new GetSocialOAuthRes(socialUser);

        return socialOAuthRes;
    }

    public boolean checkSocialUserByOauthId(String oauthId, Constant.SocialLoginType socialLoginType){
        Optional<SocialUser> socialUser = socialUserRepository.findByOauthIdAndOauthType(oauthId, socialLoginType);
        if(socialUser.isPresent()){
            return true;
        }
        return false;
    }
}
