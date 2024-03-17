package com.example.demo.src.user.model;

import com.example.demo.common.Role;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    private Long id;
    private String email;
    private String phoneNumber;
    private String password;

    private String name;
    private String nickname;


    private LocalDate birthDay;

    private boolean servicePolicyAgreed;

    private boolean dataPolicyAgreed;

    private boolean locationPolicyAgreed;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .password(this.password)
                .name(this.name)
                .nickname(this.nickname)
                .role(Role.MEMBER)
                .birthDay(this.birthDay)
                .servicePolicyAgreed(servicePolicyAgreed)
                .dataPolicyAgreed(dataPolicyAgreed)
                .locationPolicyAgreed(locationPolicyAgreed)
                .pwUpdateAt(LocalDateTime.now())
                .policyAgreedAt(LocalDateTime.now())
                .build();
    }

}
