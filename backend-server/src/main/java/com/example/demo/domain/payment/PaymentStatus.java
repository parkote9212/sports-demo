package com.example.demo.domain.payment;

public enum PaymentStatus {
    WAIT,   // 입금 대기 (무통장)
    PAID,   // 결제 완료 (포인트, 카드, 입금확인)
    CNCL    // 취소
}