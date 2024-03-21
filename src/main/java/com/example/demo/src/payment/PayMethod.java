package com.example.demo.src.payment;

import lombok.Getter;

@Getter
public enum PayMethod {
    CARD("card"),
    SAMSUNG("samsung"),
    KAKAOPAY("kakaopay");

    private final String name;

    PayMethod(String name) {
        this.name = name;
    }
}
