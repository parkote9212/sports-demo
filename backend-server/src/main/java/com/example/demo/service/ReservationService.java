package com.example.demo.service;

import com.example.demo.domain.member.Member;
import com.example.demo.domain.reservation.Reservation;
import com.example.demo.domain.reservation.ReservationStatus;
import com.example.demo.domain.schedule.Schedule;
import com.example.demo.domain.ticket.MemberTicket;
import com.example.demo.domain.ticket.TicketStatus;
import com.example.demo.dto.ProxyReservationRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MemberTicketRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberTicketRepository memberTicketRepository;

    /**
     * 관리자 대리 예약 (Proxy Reservation)
     */
    public void createProxyReservation(ProxyReservationRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 스케줄 조회
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));

        // 3. 검증: 이미 예약했는지?
        if (reservationRepository.existsByMemberIdAndScheduleId(member.getId(), schedule.getId())) {
            throw new IllegalStateException("이미 예약된 회원입니다.");
        }

        // 4. 검증: 정원 초과 여부? (Entity에 비즈니스 로직 위임 권장)
        if (schedule.getCurrentCount() >= schedule.getMaxCount()) {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }

        // 5. 이용권 차감 (가장 오래된 유효 이용권 사용)
        MemberTicket ticket = memberTicketRepository
                .findFirstByMemberIdAndStatusAndRemainingCountGreaterThanOrderByStartDateAsc(
                        member.getId(), TicketStatus.ACT, 0
                )
                .orElseThrow(() -> new IllegalStateException("사용 가능한 이용권이 없습니다."));

        ticket.useTicket(); // 잔여 횟수 -1 (Dirty Checking으로 자동 DB 반영)

        // 6. 예약 생성 및 저장
        Reservation reservation = Reservation.builder()
                .member(member)
                .schedule(schedule)
                .ticket(ticket)
                .status(ReservationStatus.RSV)
                .build();

        reservationRepository.save(reservation);

        // 7. 스케줄 예약 인원 증가 (JPA Dirty Checking)
        schedule.increaseCurrentCount();
    }
}