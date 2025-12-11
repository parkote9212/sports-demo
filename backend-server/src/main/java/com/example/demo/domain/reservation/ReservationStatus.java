package com.example.demo.domain.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    RSV("예약됨 (Reserved)"),  // 기본 상태
    ATD("출석 (Attended)"),    // 출석 완료
    NOS("노쇼 (No-Show)"),     // 미참석
    CNCL("취소 (Canceled)");   // 예약 취소됨

    private final String description;
}