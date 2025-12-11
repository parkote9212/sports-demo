package com.example.demo.domain.payment;

public enum PaymentMethod {
    POINT,          // 포인트 차감
    BANK_TRANSFER,  // 무통장 입금
    CARD            // (이번엔 구현 안 함, 확장용)
}