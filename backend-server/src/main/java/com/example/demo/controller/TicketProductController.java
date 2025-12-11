package com.example.demo.controller;

import com.example.demo.repository.TicketProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-products")
@RequiredArgsConstructor
@Tag(name = "Ticket Product API", description = "이용권 상품 관련 기능")
public class TicketProductController {

    private final TicketProductRepository ticketProductRepository;

    @GetMapping
    @Operation(summary = "이용권 상품 목록 조회", description = "판매 중인 이용권 상품 목록을 반환합니다.")
    public ResponseEntity<List<com.example.demo.domain.ticket.TicketProduct>> getTicketProducts() {
        return ResponseEntity.ok(ticketProductRepository.findBySaleYn(true));
    }

    @GetMapping("/admin/all")
    @Operation(summary = "전체 이용권 상품 조회 (관리자)", description = "관리자용 전체 이용권 상품 목록을 반환합니다.")
    public ResponseEntity<List<com.example.demo.domain.ticket.TicketProduct>> getAllTicketProducts() {
        return ResponseEntity.ok(ticketProductRepository.findAll());
    }
}