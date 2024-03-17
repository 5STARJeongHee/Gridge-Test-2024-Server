package com.example.demo.src.user;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.SecurityUser;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, BaseException {
        SecurityUser user= null;
        if(ValidationRegex.isRegexEmail(username)){
            user = userRepository.findByEmail(username)
                    .map(SecurityUser::new)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        }else if(ValidationRegex.isRegexPhone(username)){
            user = userRepository.findByPhoneNumber(username)
                    .map(SecurityUser::new)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        }
        return user;
    }
}
