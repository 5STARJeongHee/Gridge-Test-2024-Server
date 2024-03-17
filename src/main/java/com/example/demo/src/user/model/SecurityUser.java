package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.common.Role;
import com.example.demo.src.user.entity.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


public class SecurityUser implements UserDetails {
    private Long id;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
    private LocalDateTime pwUpdateAt;
    private LocalDateTime lastAccessedAt;
    protected State state;
    private boolean servicePolicyAgreed;

    private boolean dataPolicyAgreed;

    private boolean locationPolicyAgreed;
    public enum State {
        ACTIVE, INACTIVE, LOCK;
    }

    @Builder
    public SecurityUser(Long id, String email, String phoneNumber, String password, Role role, LocalDateTime pwUpdateAt, LocalDateTime lastAccessedAt, State state, boolean servicePolicyAgreed, boolean dataPolicyAgreed, boolean locationPolicyAgreed) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.pwUpdateAt = pwUpdateAt;
        this.lastAccessedAt = lastAccessedAt;
        this.state = state;
        this.servicePolicyAgreed = servicePolicyAgreed;
        this.dataPolicyAgreed = dataPolicyAgreed;
        this.locationPolicyAgreed = locationPolicyAgreed;
    }

    public SecurityUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.pwUpdateAt = user.getPwUpdateAt();
        this.lastAccessedAt = user.getLastAccessedAt();
        this.state = State.valueOf(user.getState().name());
        this.servicePolicyAgreed = user.isServicePolicyAgreed();
        this.dataPolicyAgreed = user.isDataPolicyAgreed();
        this.locationPolicyAgreed = user.isLocationPolicyAgreed();
    }
    public boolean isServicePolicyAgreed(){
        return this.servicePolicyAgreed;
    }
    public boolean isDataPolicyAgreed(){
        return this.dataPolicyAgreed;
    }
    public boolean isLocationPolicyAgreed(){
        return this.locationPolicyAgreed;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> authoritySet = new HashSet<>();
        authoritySet.add(new SimpleGrantedAuthority("ROLE_"+ this.role.toString()));
        return authoritySet;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email != null? this.email: this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !lastAccessedAt.isBefore(LocalDateTime.now().minusDays(Constant.USER_EXPIRED_DATE));
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.state.equals(State.LOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !pwUpdateAt.isBefore(LocalDateTime.now().minusDays(Constant.PASSWORD_EXPIRED_DATE));
    }

    @Override
    public boolean isEnabled() {
        return !this.state.equals(State.INACTIVE);
    }
}
