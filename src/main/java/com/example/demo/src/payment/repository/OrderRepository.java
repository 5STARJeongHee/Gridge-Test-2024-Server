package com.example.demo.src.payment.repository;

import com.example.demo.src.payment.OrderStatus;
import com.example.demo.src.payment.entity.Orders;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    Optional<Orders> findById(UUID id);
    Optional<Orders> findByIdAndOrderStatus(UUID id, OrderStatus orderStatus);
    List<Orders> findAllByUserOrderByCreatedAtDesc(User user);

}
