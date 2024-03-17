package com.example.demo.src.user;

import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Component
public class ServiceTermsScheduler {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 03 * * * ")
    public void initServiceTerms(){
        List<User> userList = userRepository.findAllByState(ACTIVE);
        userList.forEach(
                user -> {
                    if(user.getPolicyAgreedAt().isBefore(LocalDateTime.now().minusDays(365))){
                        user.updatePolicy(false, false, false);
                    }
                }
        );
    }
}
