package com.example.demo.domain.ticket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketStatus {

    ACT("활성 (Active)"),     // 사용 가능
    EXP("만료 (Expired)"),    // 기간 지남
    EXH("소진 (Exhausted)"),  // 횟수 다 씀
    STP("정지 (Stopped)");    // 일시 정지

    private final String description;
}