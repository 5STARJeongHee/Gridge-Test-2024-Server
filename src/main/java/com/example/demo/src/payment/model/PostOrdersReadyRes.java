package com.example.demo.src.payment.model;

import com.example.demo.src.payment.OrderStatus;
import com.example.demo.src.payment.PayMethod;
import com.example.demo.src.payment.Product;
import com.example.demo.src.payment.entity.Orders;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PostOrdersReadyRes {
    private UUID merchant_uid;
    private String pgCode;
    private PayMethod payMethod;
    private String productName;
    private long amount;
    private String userId;
    private OrderStatus orderStatus;

    @Builder
    public PostOrdersReadyRes(UUID merchant_uid, String pgCode, PayMethod payMethod, String productName, long amount, String userId, OrderStatus orderStatus) {
        this.merchant_uid = merchant_uid;
        this.pgCode = pgCode;
        this.payMethod = payMethod;
        this.productName = productName;
        this.amount = amount;
        this.userId = userId;
        this.orderStatus = orderStatus;
    }

    public PostOrdersReadyRes(Orders orders){
        this.merchant_uid = orders.getMerchantUid();
        this.pgCode = orders.getPgCode();
        this.payMethod = orders.getPayMethod();
        this.productName = Product.MONTH_SUBSCRIPTION.getProductNm();
        this.amount = orders.getAmount();
        this.userId = orders.getUser() != null ? orders.getUser().getEmail(): "";
        this.orderStatus = orders.getOrderStatus();
    }
}
