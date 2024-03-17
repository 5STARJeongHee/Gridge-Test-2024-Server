package com.example.demo.src.user.model;

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
public class PatchServiceTermsRes {
    private long id;
    private boolean servicePolicyAgreed;

    private boolean dataPolicyAgreed;

    private boolean locationPolicyAgreed;

    private LocalDateTime policyAgreedAt;

    public PatchServiceTermsRes(User user){
        this.id = user.getId();
        this.servicePolicyAgreed = user.isServicePolicyAgreed();
        this.dataPolicyAgreed = user.isDataPolicyAgreed();
        this.locationPolicyAgreed = user.isLocationPolicyAgreed();
        this.policyAgreedAt = user.getPolicyAgreedAt();
    }
}
