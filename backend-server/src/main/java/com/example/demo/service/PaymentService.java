package com.example.demo.service;

import com.example.demo.domain.member.Member;
import com.example.demo.domain.payment.Payment;
import com.example.demo.domain.payment.PaymentMethod;
import com.example.demo.domain.payment.PaymentStatus;
import com.example.demo.domain.ticket.MemberTicket;
import com.example.demo.domain.ticket.TicketProduct;
import com.example.demo.domain.ticket.TicketStatus;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MemberTicketRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.TicketProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final TicketProductRepository productRepository;
    private final MemberTicketRepository memberTicketRepository;

    /**
     * 1. 결제 요청 처리 (포인트는 즉시 완료, 무통장은 대기)
     */
    public Long processPayment(PaymentRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));
        TicketProduct product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // A. 포인트 결제: 잔액 확인 -> 차감 -> 즉시 티켓 발급
        if (request.method() == PaymentMethod.POINT) {
            // Member 엔티티의 usePoint() 메서드 호출 (비즈니스 로직 위임)
            member.usePoint(product.getPrice());

            // 결제 이력 저장 (PAID)
            Payment payment = savePayment(member, product, PaymentMethod.POINT, PaymentStatus.PAID);

            // 티켓 즉시 발급
            issueTicket(member, product);

            return payment.getId();
        }

        // B. 무통장 입금: 결제 이력만 저장 (WAIT) -> 티켓 발급 X
        else if (request.method() == PaymentMethod.BANK_TRANSFER) {
            Payment payment = savePayment(member, product, PaymentMethod.BANK_TRANSFER, PaymentStatus.WAIT);
            return payment.getId();
        }

        throw new IllegalArgumentException("지원하지 않는 결제 수단");
    }

    /**
     * 2. [관리자] 무통장 입금 확인 및 승인
     */
    public void confirmBankTransfer(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("이미 처리된 결제입니다.");
        }

        // 상태 변경 (Wait -> Paid)
        payment.confirmPayment(); // Dirty Checking

        // 티켓 발급
        issueTicket(payment.getMember(), payment.getProduct());
    }

    // --- 내부 헬퍼 메서드 ---

    private Payment savePayment(Member member, TicketProduct product, PaymentMethod method, PaymentStatus status) {
        Payment payment = Payment.builder()
                .orderNo(UUID.randomUUID().toString().substring(0, 8)) // 데모용 랜덤 주문번호
                .member(member)
                .product(product)
                .amount(product.getPrice())
                .method(method)
                .status(status)
                .build();
        return paymentRepository.save(payment);
    }

    private void issueTicket(Member member, TicketProduct product) {
        MemberTicket ticket = MemberTicket.builder()
                .member(member)
                .product(product)
                .remainingCount(product.getProvidedCount())
                .status(TicketStatus.ACT)
                // 유효기간: 오늘부터 90일 (데모 정책)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(90))
                .build();
        memberTicketRepository.save(ticket);
    }
}