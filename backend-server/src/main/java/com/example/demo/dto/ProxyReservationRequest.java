package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record ProxyReservationRequest(
        @NotNull(message = "스케줄 ID는 필수입니다.")
        Long scheduleId,

        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId
) {}