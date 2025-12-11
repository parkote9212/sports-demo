package com.example.demo.dto;

import com.example.demo.domain.payment.Payment;

import java.time.LocalDateTime;

public record PendingPaymentResponse(
        Long paymentId,
        String orderNo,
        String memberName,
        String productName,
        int amount,
        LocalDateTime createdAt
) {
    public static PendingPaymentResponse from(Payment payment) {
        return new PendingPaymentResponse(
                payment.getId(),
                payment.getOrderNo(),
                payment.getMember().getName(),
                payment.getProduct().getName(),
                payment.getAmount(),
                payment.getRegDt()
        );
    }
}