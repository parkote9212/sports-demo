package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "결제 및 구매")
public class PaymentController {

    private final PaymentService paymentService;
    private final com.example.demo.repository.MemberRepository memberRepository;
    private final com.example.demo.repository.PaymentRepository paymentRepository;

    // [User] 캐시 포인트 조회
    @GetMapping("/members/{memberId}/cash-point")
    @Operation(summary = "캐시 포인트 조회", description = "회원의 남은 캐시 포인트를 조회합니다.")
    public ResponseEntity<Integer> getCashPoint(@PathVariable Long memberId) {
        int cashPoint = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."))
                .getCashPoint();
        return ResponseEntity.ok(cashPoint);
    }

    // [User] 이용권 구매 요청
    @PostMapping("/payments/purchase")
    @Operation(summary = "이용권 구매 (포인트/무통장)", description = "POINT: 즉시 차감 및 발급, BANK_TRANSFER: 대기 상태 생성")
    public ResponseEntity<String> purchaseTicket(@RequestBody @Valid PaymentRequest request) {
        Long paymentId = paymentService.processPayment(request);
        return ResponseEntity.ok("주문이 접수되었습니다. ID: " + paymentId);
    }

    // [Admin] 무통장 입금 대기 목록 조회
    @GetMapping("/admin/payments/pending")
    @Operation(summary = "무통장 입금 대기 목록", description = "관리자가 승인해야 할 무통장 입금 대기 목록을 조회합니다.")
    public ResponseEntity<java.util.List<com.example.demo.dto.PendingPaymentResponse>> getPendingPayments() {
        java.util.List<com.example.demo.dto.PendingPaymentResponse> response = paymentRepository
                .findByMethodAndStatusWithDetails(com.example.demo.domain.payment.PaymentMethod.BANK_TRANSFER, 
                                                com.example.demo.domain.payment.PaymentStatus.WAIT)
                .stream()
                .map(com.example.demo.dto.PendingPaymentResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    // [Admin] 무통장 입금 승인
    @PostMapping("/admin/payments/{paymentId}/confirm")
    @Operation(summary = "무통장 입금 승인", description = "관리자가 입금을 확인하고 승인하면 이용권이 발급됩니다.")
    public ResponseEntity<String> confirmPayment(@PathVariable Long paymentId) {
        paymentService.confirmBankTransfer(paymentId);
        return ResponseEntity.ok("입금 확인 완료. 이용권이 발급되었습니다.");
    }
}