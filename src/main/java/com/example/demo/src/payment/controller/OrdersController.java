package com.example.demo.src.payment.controller;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.payment.service.OrdersService;
import com.example.demo.src.payment.model.GetOrderCompleteReq;
import com.example.demo.src.payment.model.GetOrderCompleteRes;
import com.example.demo.src.payment.model.PostOrdersReadyReq;
import com.example.demo.src.payment.model.PostOrdersReadyRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.common.response.BaseResponseStatus.SERVER_ERROR;

@Tag(name = "결제", description = "결제 관련 api")
@RequiredArgsConstructor
@RequestMapping("/app/order/*")
@RestController
public class OrdersController {

    private final OrdersService ordersService;
    @Operation(summary = "결제 요청 전 사전결제 api", description = "사전 결제 정보를 저장 후 결재 서버에 사전 결재 정보인 주문 id와 구매 가격을 전달한다. ")
    @PostMapping("preOrder")
    public BaseResponse<PostOrdersReadyRes> readyOrder(@RequestBody PostOrdersReadyReq postOrdersReadyReq, @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : email") String userId){
        if(postOrdersReadyReq.getPgCode() == null){
            throw new BaseException(BaseResponseStatus.POST_EMPTY_PGCODE);
        }
        if(postOrdersReadyReq.getPayMethod() == null){
            throw new BaseException(BaseResponseStatus.POST_EMPTY_PAYMETHOD);
        }
        if(userId == null || userId.equals("")){
            throw new BaseException(BaseResponseStatus.EXPIRE_AUTHENTICATION);
        }
        postOrdersReadyReq.setUserId(userId);
        PostOrdersReadyRes orderReadyRes = ordersService.createOrderReady(postOrdersReadyReq);
        return new BaseResponse<>(orderReadyRes);
    }

    @Operation(summary = "결제 요청 후 사후검증 api", description = "결제 요청에 사용된 주문 번호를 통한 결제 서버의 결제 정보를 단일 조회하여 사후 검증한다.")
    @GetMapping("complete")
    public BaseResponse<GetOrderCompleteRes> completeOrder(GetOrderCompleteReq getOrderCompleteReq, @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : email") String userId){
        if(getOrderCompleteReq.getMerchant_uid() == null || getOrderCompleteReq.getMerchant_uid().equals("")){
            throw new BaseException(SERVER_ERROR);
        }
        getOrderCompleteReq.setUserId(userId);
        GetOrderCompleteRes orderCompleteRes = ordersService.createOrderComplete(getOrderCompleteReq);

        return new BaseResponse<>(orderCompleteRes);
    }
}
