package com.example.demo.src.user.entity;

import com.example.demo.common.Role;
import com.example.demo.common.State;
import com.example.demo.common.jpa.AESConverter;
import com.example.demo.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = AESConverter.class)
    @Column(length = 100, unique = true)
    private String email;

    @Convert(converter = AESConverter.class)
    @Column(length = 30, unique = true)
    private String phoneNumber;

    @Convert(converter = AESConverter.class)
    @Column(length = 100, nullable = false)
    private String name;

    @Column
    private LocalDate birthDay;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 10)
    private Role role;

    @Column(name = "pwUpdateAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime pwUpdateAt;

    @Column(name = "lastAccessedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastAccessedAt;

    @Column(nullable = false)
    private boolean servicePolicyAgreed;

    @Column(nullable = false)
    private boolean dataPolicyAgreed;

    @Column(nullable = false)
    private boolean locationPolicyAgreed;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime policyAgreedAt;

    @Builder
    public User(Long id, String email, String phoneNumber, String name, LocalDate birthDay, String password, String nickname, Role role, LocalDateTime pwUpdateAt, LocalDateTime lastAccessedAt, boolean servicePolicyAgreed, boolean dataPolicyAgreed, boolean locationPolicyAgreed, LocalDateTime policyAgreedAt) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.birthDay = birthDay;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.pwUpdateAt = pwUpdateAt;
        this.lastAccessedAt = lastAccessedAt;
        this.servicePolicyAgreed = servicePolicyAgreed;
        this.dataPolicyAgreed = dataPolicyAgreed;
        this.locationPolicyAgreed = locationPolicyAgreed;
        this.policyAgreedAt = policyAgreedAt;
    }

    public void updateProfile(String name, String nickname, String phoneNumber, LocalDate birthDay ) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
    }
    public void updatePwUpdateAt(LocalDateTime localDateTime){
        pwUpdateAt = localDateTime;
    }
    public void updateLastAccessedAt(LocalDateTime localDateTime) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public void updatePolicy(boolean servicePolicyAgreed, boolean dataPolicyAgreed, boolean locationPolicyAgreed){
        this.servicePolicyAgreed = servicePolicyAgreed;
        this.dataPolicyAgreed = dataPolicyAgreed;
        this.locationPolicyAgreed = locationPolicyAgreed;
        this.policyAgreedAt = LocalDateTime.now();
    }
    public void updateRole(Role role){
        this.role = role;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }

    public void updateEmail(String email){
        this.email = email;
    }
    public void updatePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void lockUser() {
        this.state = State.LOCK;
    }

}
