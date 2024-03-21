package com.example.demo.src.payment.service;

import com.example.demo.common.State;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.payment.OrderStatus;
import com.example.demo.src.payment.PGAgentProperties;
import com.example.demo.src.payment.entity.Orders;
import com.example.demo.src.payment.model.*;
import com.example.demo.src.payment.repository.OrderRepository;
import com.example.demo.src.token.model.PortOneAccessTokenRes;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final PGAgentProperties properties;
    public PostOrdersReadyRes createOrderReady(PostOrdersReadyReq orderReadyReq){
        if(orderReadyReq.getPgCode() == null || orderReadyReq.getPgCode().equals("")){
            throw new BaseException(POST_EMPTY_PGCODE);
        }
        if(orderReadyReq.getPayMethod() == null || orderReadyReq.getPayMethod().equals("")){
            throw new BaseException(POST_EMPTY_PAYMETHOD);
        }
        User user = userRepository.findByEmail(orderReadyReq.getUserId())
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        Orders req = orderReadyReq.toEntity();
        req.updateUser(user);
        Orders result = orderRepository.save(req);
        try {
            requestPreOrder(result.getMerchantUid(), result.getAmount());
        } catch (RestClientException | JsonProcessingException e){
            throw new BaseException(FAILED_PREORDER);
        }

        return new PostOrdersReadyRes(result);
    }

    public GetOrderCompleteRes createOrderComplete(GetOrderCompleteReq orderCompleteReq){
        GetPortOneOrderRes response = null;
        // 사후 검증 정보 받아오기
        try{
            response = requestComplete(UUID.fromString(orderCompleteReq.getMerchant_uid()));

        }catch (RestClientException | JsonProcessingException e){
            throw new BaseException(INVALID_ORDER);
        }
        // DB에서 사전 결제 정보 받아오기
        Orders preOrder = orderRepository.findByIdAndOrderStatus(UUID.fromString(response.getData().getMerchant_uid()), OrderStatus.PAYMENT_READY)
                .orElse(null);
        // 결제 정보 자체가 없음
        if(preOrder == null){
            throw new BaseException(INVALID_ORDER);
        }
        Orders completeOrder = null;
        if(preOrder.getAmount() == response.getData().getAmount()){
            if(response.getData().getStatus().equals("paid")){
                completeOrder = Orders.builder()
                        .merchantUid(preOrder.getMerchantUid())
                        .pgCode(preOrder.getPgCode())
                        .productId(preOrder.getProductId())
                        .payMethod(preOrder.getPayMethod())
                        .impId(orderCompleteReq.getImp_uid())
                        .state(State.ACTIVE)
                        .user(preOrder.getUser())
                        .orderStatus(OrderStatus.PAYMENT_SUCCESS)
                        .build();

            } else if(response.getData().getStatus().equals("ready")){

            } else {
                completeOrder = Orders.builder()
                        .merchantUid(preOrder.getMerchantUid())
                        .pgCode(preOrder.getPgCode())
                        .productId(preOrder.getProductId())
                        .payMethod(preOrder.getPayMethod())
                        .impId(orderCompleteReq.getImp_uid())
                        .state(State.ACTIVE)
                        .user(preOrder.getUser())
                        .orderStatus(OrderStatus.PAYMENT_FAIL)
                        .build();
            }
        }else {
            completeOrder = Orders.builder()
                    .merchantUid(preOrder.getMerchantUid())
                    .pgCode(preOrder.getPgCode())
                    .productId(preOrder.getProductId())
                    .payMethod(preOrder.getPayMethod())
                    .impId(orderCompleteReq.getImp_uid())
                    .state(State.ACTIVE)
                    .user(preOrder.getUser())
                    .orderStatus(OrderStatus.PAYMENT_FAIL)
                    .build();
        }
        if(completeOrder != null){
            orderRepository.save(completeOrder);
            return new GetOrderCompleteRes(completeOrder.getMerchantUid().toString(), completeOrder.getOrderStatus());
        }
        return new GetOrderCompleteRes(orderCompleteReq.getMerchant_uid(), OrderStatus.PAYMENT_FAIL);
    }
    private PortOneAccessTokenRes requestGetToken() throws JsonProcessingException {
        final String PORT_ONE_TOKEN_URL = "https://api.iamport.kr/users/getToken";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");

        Map<String, Object> map= new HashMap<>();
        map.put("imp_key", properties.getMerchant().getRestApiKey());
        map.put("imp_secret", properties.getMerchant().getRestApiSecret());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(PORT_ONE_TOKEN_URL, request, String.class);
        PortOneAccessTokenRes result = objectMapper.readValue(response.getBody(), PortOneAccessTokenRes.class);
        return result;
    }

    private GetPortOneOrderRes requestComplete(UUID merchantUid) throws JsonProcessingException {
        final String PORT_ONE_SELECT_ONE_URL = "https://api.iamport.kr/payments/"+merchantUid.toString();
        String accessToken = requestGetToken().getResponse().getAccess_token();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer "+accessToken);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PORT_ONE_SELECT_ONE_URL,
                HttpMethod.GET,
                request,
                String.class
        );
        GetPortOneOrderRes result = objectMapper.readValue(response.getBody(), GetPortOneOrderRes.class);
        return result;
    }
    private ResponseEntity<String> requestPreOrder(UUID merchantUid, long amount) throws RestClientException, JsonProcessingException {
        final String PORT_ONE_PRE_ORDER_URL = "https://api.iamport.kr/payments/prepare";

        String accessToken = requestGetToken().getResponse().getAccess_token();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer "+accessToken);
        Map<String, Object> map= new HashMap<>();
        map.put("merchant_uid", merchantUid.toString());
        map.put("amount", String.valueOf(amount));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(PORT_ONE_PRE_ORDER_URL, request, String.class);
        return response;
    }
}
