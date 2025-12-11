package com.example.demo.domain.payment;

import com.example.demo.domain.common.BaseTimeEntity;
import com.example.demo.domain.member.Member;
import com.example.demo.domain.ticket.TicketProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAY_ID")
    private Long id;

    @Column(name = "ORD_NO", unique = true)
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_ID")
    private Member member;

    // [추가] 어떤 상품을 결제했는지 알아야 나중에 티켓을 발급함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROD_ID")
    private TicketProduct product;

    @Column(name = "PAY_AMT")
    private int amount;

    @Column(name = "STTS_CD")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "PAY_MTHD_CD")
    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // [추가] 결제 수단

    @Builder
    public Payment(String orderNo, Member member, TicketProduct product, int amount, PaymentStatus status, PaymentMethod method) {
        this.orderNo = orderNo;
        this.member = member;
        this.product = product;
        this.amount = amount;
        this.status = status;
        this.method = method;
    }

    // 비즈니스 로직: 결제 완료 처리 (관리자 승인 시 사용)
    public void confirmPayment() {
        this.status = PaymentStatus.PAID;
    }
}