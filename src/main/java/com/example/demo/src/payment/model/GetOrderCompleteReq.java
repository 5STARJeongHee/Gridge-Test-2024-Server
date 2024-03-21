package com.example.demo.src.payment.model;

import com.example.demo.src.payment.OrderStatus;
import com.example.demo.src.payment.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GetOrderCompleteReq {
    private String imp_uid;
    private String merchant_uid;
    private boolean imp_success;
    private String userId;


}
