package com.example.demo.src.user;

import com.example.demo.common.Constant.*;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class OauthLoginController {
    private final UserService userService;

    private final OAuthService oAuthService;

    private final JwtService jwtService;

    @GetMapping("")
    public String login(){
        return "login";
    }

    @GetMapping("/redirect/{socialLoginType}")
    public String socialLogin(@PathVariable("socialLoginType") String socialLoginType){
        return "redirect:/app/users/auth/"+ SocialLoginType.valueOf(socialLoginType.toUpperCase())+"/login";
    }

    @GetMapping("/{socialLoginType}")
    public String socialLoginCallBack(Model model, @PathVariable("socialLoginType") String socialLoginType, @RequestParam("code") String code){
        return "redirect:/app/users/auth/"+ SocialLoginType.valueOf(socialLoginType.toUpperCase())+"/login/callback?code="+code;
    }
}
