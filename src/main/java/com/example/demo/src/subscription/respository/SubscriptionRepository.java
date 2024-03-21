package com.example.demo.src.subscription.respository;

import com.example.demo.common.State;
import com.example.demo.src.subscription.entity.Subscription;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<SubscriptionRepository> findAllByUserAndState(User user, State state);
    Optional<Subscription> findFirstByUserOrderByCreatedAtDesc(User user);
}
