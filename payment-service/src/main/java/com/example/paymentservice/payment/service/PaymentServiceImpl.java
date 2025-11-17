package com.example.paymentservice.payment.service;

import com.example.paymentservice.payment.service.dto.response.PayRechargeResult;
import com.example.paymentservice.payment.service.dto.response.PaymentGetResult;
import com.example.paymentservice.payment.service.dto.response.PaymentsGetResult;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PayRechargeResult payForRecharge(String code, String amount) {
        return null;
    }

    @Override
    public PaymentsGetResult getAllPayments(String code) {
        return null;
    }

    @Override
    public PaymentGetResult getPaymentByOrderCode(String code, String orderPgId) {
        return null;
    }
}
