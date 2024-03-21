package com.example.demo.src.user.repository;

import com.example.demo.src.user.entity.SocialUser;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.common.Constant.*;
import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, String> {
    Optional<SocialUser> findByUserAndOauthType(User user, SocialLoginType socialLoginType);
    Optional<SocialUser> findByOauthIdAndOauthType(String oauthId, SocialLoginType socialLoginType);
}
