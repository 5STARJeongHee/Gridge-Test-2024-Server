package com.example.demo.src.payment.repository;

import com.example.demo.src.payment.entity.BillingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingKeyRepository extends JpaRepository<BillingKey, UUID> {

}
