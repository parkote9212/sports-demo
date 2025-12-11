package com.example.demo.controller;

import com.example.demo.dto.ProxyReservationRequest;
import com.example.demo.dto.ScheduleAdminDto;
import com.example.demo.dto.TicketIssueRequest;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.service.ReservationService;
import com.example.demo.service.TicketAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "관리자 전용 기능")
public class AdminController {

    private final AdminMapper adminMapper;
    private final ReservationService reservationService;
    private final TicketAdminService ticketAdminService;
    private final com.example.demo.repository.ReservationRepository reservationRepository;
    private final com.example.demo.repository.MemberTicketRepository memberTicketRepository;

    @GetMapping("/member-tickets")
    @Operation(summary = "회원 이용권 전체 조회", description = "모든 회원의 이용권 목록을 조회합니다.")
    public ResponseEntity<List<com.example.demo.dto.MemberTicketAdminResponse>> getAllMemberTickets() {
        List<com.example.demo.dto.MemberTicketAdminResponse> response = memberTicketRepository
                .findAllWithDetails()
                .stream()
                .map(com.example.demo.dto.MemberTicketAdminResponse::from)
                .toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations")
    @Operation(summary = "예약 목록 조회", description = "전체 예약 목록 또는 특정 날짜 예약 목록을 조회합니다.")
    public ResponseEntity<List<com.example.demo.dto.ReservationAdminResponse>> getReservations(
            @RequestParam(value = "date", required = false) String date // YYYY-MM-DD 형식, 없으면 전체 조회
    ) {
        List<com.example.demo.domain.reservation.Reservation> reservations;
        
        if (date != null) {
            java.time.LocalDateTime startDate = java.time.LocalDate.parse(date).atStartOfDay();
            java.time.LocalDateTime endDate = startDate.plusDays(1);
            reservations = reservationRepository.findByDateWithDetails(startDate, endDate);
        } else {
            reservations = reservationRepository.findAllWithDetails();
        }
        
        List<com.example.demo.dto.ReservationAdminResponse> response = reservations.stream()
                .map(com.example.demo.dto.ReservationAdminResponse::from)
                .toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedules")
    @Operation(summary = "스케줄 목록 조회", description = "전체 스케줄 목록 또는 특정 날짜 스케줄을 조회합니다. branchId를 넘기면 해당 지점만 필터링됩니다.")
    public ResponseEntity<List<ScheduleAdminDto>> getSchedules(
            @RequestParam(value = "date", required = false) String date, // YYYY-MM-DD 형식, 없으면 전체 조회
            @RequestParam(value = "branchId", required = false) Long branchId
    ) {
        return ResponseEntity.ok(adminMapper.selectDailySchedules(date, branchId));
    }

    @PostMapping("/reservations")
    @Operation(summary = "관리자 대리 예약", description = "관리자가 회원을 특정 수업에 강제로 예약시킵니다.")
    public ResponseEntity<Void> createProxyReservation(@RequestBody @Valid ProxyReservationRequest request) {
        reservationService.createProxyReservation(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tickets")
    @Operation(summary = "이용권 수동 발급", description = "관리자가 회원에게 임의의 이용권을 생성하여 지급합니다.")
    public ResponseEntity<Void> issueTicket(@RequestBody @Valid TicketIssueRequest request) {
        ticketAdminService.issueTicket(request);
        return ResponseEntity.ok().build();
    }
}