package com.example.demo.src.payment.model;

import com.example.demo.common.State;
import com.example.demo.src.payment.OrderStatus;
import com.example.demo.src.payment.PayMethod;
import com.example.demo.src.payment.Product;
import com.example.demo.src.payment.entity.Orders;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostOrdersReadyReq {
    private String pgCode;
    private String payMethod;
    private String userId;

    @Builder
    public PostOrdersReadyReq(String pgCode, String payMethod) {
        this.pgCode = pgCode;
        this.payMethod = payMethod;
    }

    public Orders toEntity(){
        return Orders.builder()
                .productId(Product.MONTH_SUBSCRIPTION.getId())
                .amount(Product.MONTH_SUBSCRIPTION.getPrice())
                .pgCode(pgCode)
                .payMethod(PayMethod.valueOf(payMethod.toUpperCase()))
                .state(State.ACTIVE)
                .orderStatus(OrderStatus.PAYMENT_READY)
                .build();
    }
}

