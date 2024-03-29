package com.example.demo.src.user.model;


import com.example.demo.common.Role;
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
public class GetUserRes {
    private Long id;
    private String email;
    private String phoneNumber;
    private String nickname;
    private String name;
    private Role role;

    private boolean servicePolicyAgreed;

    private boolean dataPolicyAgreed;

    private boolean locationPolicyAgreed;

    private LocalDateTime policyAgreedAt;

    public GetUserRes(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.role = user.getRole();
        this.servicePolicyAgreed = user.isServicePolicyAgreed();
        this.dataPolicyAgreed = user.isDataPolicyAgreed();
        this.locationPolicyAgreed = user.isLocationPolicyAgreed();
        this.policyAgreedAt = user.getPolicyAgreedAt();
    }
}
