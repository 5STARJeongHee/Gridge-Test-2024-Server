package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserRes {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;

    public PostUserRes(User user){
        id = user.getId();
        name = user.getName();
        nickname = user.getNickname();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
    }
}
