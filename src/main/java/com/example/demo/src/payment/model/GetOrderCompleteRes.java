package com.example.demo.src.payment.model;

import com.example.demo.src.payment.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetOrderCompleteRes {
    private String merchant_uid;
    private OrderStatus orderStatus;
}
