package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.common.Role;
import com.example.demo.src.user.entity.SocialUser;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {
    public String id;
    public Properties properties;
    public LocalDateTime connected_at;
    public KakaoAcount kakao_account;

    public static class KakaoAcount{
        public Profile profile;
        public String email;
    }
    public static class Profile {
        public String nickname;
    }

    public static class Properties {
        public String nickname;
    }

    public User toEntity(){
        return User.builder()
                .email(this.kakao_account.email)
                .password("NONE")
                .role(Role.MEMBER)
                .build();
    }

    public SocialUser toSocialUserEntity() {
        return SocialUser.builder()
                .oauthId(id)
                .oauthType(Constant.SocialLoginType.KAKAO)
                .user(toEntity())
                .build();
    }

}
