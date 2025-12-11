package com.example.demo.dto;

import java.time.LocalDate;

public record MyTicketResponse(
        Long ticketId,
        String ticketName,      // 상품명 (ex: 헬스 PT 30회)
        String status,          // 상태 (활성, 만료 등)
        int remainingCount,     // 잔여 횟수 (15)
        int totalCount,         // 총 횟수 (30) -> 프로그레스 바 계산용
        LocalDate expiryDate,   // 만료일
        long dDay               // D-Day (서버에서 계산해서 내려줌)
) {}