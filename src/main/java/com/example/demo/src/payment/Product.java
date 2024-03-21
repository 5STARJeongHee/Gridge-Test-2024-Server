package com.example.demo.src.payment;

import lombok.Getter;

@Getter
public enum Product {
    MONTH_SUBSCRIPTION(1, "월 구독", 9900, 30),
    ;


    private final long id;
    private final String productNm;
    private final long price;
    private final int period;


    Product(long id, String productNm, long price, int period) {
        this.id = id;
        this.productNm = productNm;
        this.price = price;
        this.period = period;
    }


}
