package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchServiceTermsReq {
    private long id;
    private boolean servicePolicyAgreed;

    private boolean dataPolicyAgreed;

    private boolean locationPolicyAgreed;
}
