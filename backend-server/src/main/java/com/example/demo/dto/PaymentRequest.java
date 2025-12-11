package com.example.demo.dto;

import com.example.demo.domain.payment.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull Long memberId,
        @NotNull Long productId,
        @NotNull PaymentMethod method // POINT or BANK_TRANSFER
) {}