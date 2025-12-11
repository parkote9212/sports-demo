package com.example.demo.controller;

import com.example.demo.dto.MyTicketResponse;
import com.example.demo.service.TicketService;
import io.swagger.v3.oas.annotations.Operation; // (선택사항) 문서화용
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket API", description = "이용권 관련 기능")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/my")
    @Operation(summary = "내 이용권 조회", description = "로그인한 회원의 보유 이용권 목록을 반환합니다.")
    public ResponseEntity<List<MyTicketResponse>> getMyTickets(
            @RequestHeader(value = "X-USER-ID", defaultValue = "1") Long memberId // 데모용: 헤더 없으면 1번(홍길동)으로 간주
    ) {
        List<MyTicketResponse> response = ticketService.getMyTickets(memberId);
        return ResponseEntity.ok(response);
    }
}