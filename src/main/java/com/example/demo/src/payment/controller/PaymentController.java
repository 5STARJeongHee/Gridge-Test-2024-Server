package com.example.demo.src.payment.controller;

import com.example.demo.src.payment.model.PostOrdersReadyReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@RequestMapping("/app/payment/*")
@Controller
public class PaymentController {
    @RequestMapping(name = "order", method = RequestMethod.GET)
    public String preorder(){
        return "order";
    }
}
