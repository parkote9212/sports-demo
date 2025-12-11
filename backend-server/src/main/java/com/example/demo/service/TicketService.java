package com.example.demo.service;

import com.example.demo.domain.ticket.MemberTicket;
import com.example.demo.dto.MyTicketResponse;
import com.example.demo.repository.MemberTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final MemberTicketRepository memberTicketRepository;

    /**
     * 내 이용권 목록 조회
     */
    public List<MyTicketResponse> getMyTickets(Long memberId) {
        // 1. DB에서 회원의 이용권 목록 조회 (최신순)
        List<MemberTicket> tickets = memberTicketRepository.findByMemberIdOrderByIdDesc(memberId);

        // 2. Entity -> DTO 변환 (Stream API 활용)
        return tickets.stream()
                .map(this::convertToDto)
                .toList(); // Java 16+ toList()
    }

    // 변환 로직 분리
    private MyTicketResponse convertToDto(MemberTicket ticket) {
        // D-Day 계산 (만료일 - 오늘)
        long dDay = 0;
        if (ticket.getEndDate() != null) {
            dDay = ChronoUnit.DAYS.between(LocalDate.now(), ticket.getEndDate());
        }

        // 총 횟수는 상품 정보(Product)에서 가져옴
        int totalCount = ticket.getProduct().getProvidedCount();

        return new MyTicketResponse(
                ticket.getId(),
                ticket.getProduct().getName(),
                ticket.getStatus().getDescription(), // Enum의 한글 설명 (ex: "활성")
                ticket.getRemainingCount(),
                totalCount,
                ticket.getEndDate(),
                dDay
        );
    }
}