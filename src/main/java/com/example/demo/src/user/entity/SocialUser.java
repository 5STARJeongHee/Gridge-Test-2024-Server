package com.example.demo.src.user.entity;

import com.example.demo.common.Constant.*;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "SOCIAL_USER")
public class SocialUser {
    @Id
    @Column(name = "oauth_id", nullable = false, updatable = false)
    private String oauthId;

    @Column(nullable = false)
    private SocialLoginType oauthType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="email")
    private User user;

    @Builder
    public SocialUser(String oauthId, SocialLoginType oauthType, User user) {
        this.oauthId = oauthId;
        this.oauthType = oauthType;
        this.user = user;
    }

    public void updateUser(User user){
        this.user = user;
    }
}
