package com.example.paymentservice.payment.controller;

import com.example.paymentservice.common.dto.ResponseDto;
import com.example.paymentservice.payment.controller.dto.response.PayRechargeResponse;
import com.example.paymentservice.payment.controller.dto.response.PaymentGetResponse;
import com.example.paymentservice.payment.controller.dto.response.PaymentsGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController implements PaymentApi {

    @Override
    @PostMapping("/charge")
    public ResponseEntity<ResponseDto<PayRechargeResponse>> payForRecharge(@RequestHeader(name = "X-CODE") String code,
            @PathVariable String amount) {
        return null;
    }

    @Override
    @GetMapping("/orders")
    public ResponseEntity<ResponseDto<PaymentsGetResponse>> getAllPayments(
            @RequestHeader(name = "X-CODE") String code) {
        return null;
    }

    @Override
    @GetMapping("/orders/{order-pg-id}")
    public ResponseEntity<ResponseDto<PaymentGetResponse>> getPaymentByOrderCode(
            @RequestHeader(name = "X-CODE") String code, @PathVariable("order-pg-id") String orderPgId) {
        return null;
    }
}
