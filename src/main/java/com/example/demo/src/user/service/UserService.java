package com.example.demo.src.user.service;



import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.CustomAuthenticationSuccessHandler;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {


    private final JwtService jwtService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException{
        //중복 체크
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
        User saveUser = userRepository.save(postUserReq.toEntity());
        return new PostUserRes(saveUser);
    }

    public void modifyProfile(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        user.updateProfile(patchUserReq.getName(), patchUserReq.getNickname(), patchUserReq.getPhoneNumber(), patchUserReq.getBirthDay());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();
    }

    public void lockUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.lockUser();
    }
    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    @Transactional(readOnly = true)
    public GetUserRes getUsersByPhoneNumber(String phoneNumber) {
        GetUserRes getUserRes = userRepository.findByPhoneNumberAndState(phoneNumber, ACTIVE)
                .map(GetUserRes::new)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return getUserRes;
    }

    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }




    @Transactional(readOnly = true)
    public boolean checkUserByEmail(String email) {
        Optional<User> result = userRepository.findByEmailAndState(email, ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }
    public PatchServiceTermsRes updateServiceTerms(PatchServiceTermsReq patchServiceTermsReq){
        User user = userRepository.findByIdAndState(patchServiceTermsReq.getId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updatePolicy(patchServiceTermsReq.isServicePolicyAgreed(), patchServiceTermsReq.isDataPolicyAgreed(), patchServiceTermsReq.isLocationPolicyAgreed());

        return new PatchServiceTermsRes(user);
    }
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        SecurityUser securityUser = null;
        User user = null;
        if(ValidationRegex.isRegexEmail(postLoginReq.getUserId())){
            user = userRepository.findByEmail(postLoginReq.getUserId())
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        }else{
            user = userRepository.findByPhoneNumber(postLoginReq.getUserId())
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        }
        securityUser = new SecurityUser(user);

        if(!passwordEncoder.matches(postLoginReq.getPassword(), securityUser.getPassword())){
            throw new BaseException(INVALID_PASSOWRD);
        }
        if(!securityUser.isEnabled()){
            throw new BaseException(INACTIVE_USER);
        }
        if(!securityUser.isAccountNonLocked()){
            throw new BaseException(LOCK_USER);
        }
        if(!securityUser.isAccountNonExpired()){
            throw new BaseException(EXPIRE_USER);
        }

        user.updateLastAccessedAt(LocalDateTime.now());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                "",
                securityUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        PostLoginRes loginRes = authenticationSuccessHandler.onAuthenticationSuccess(authentication);
        loginRes.setServiceTermsCheck(user.isServicePolicyAgreed() && user.isDataPolicyAgreed() && user.isLocationPolicyAgreed());
        return loginRes;
    }

    public GetUserRes getUserByEmail(String email) {
        User user = userRepository.findByEmailAndState(email, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }
}
