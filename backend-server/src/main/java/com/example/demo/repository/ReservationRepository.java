package com.example.demo.repository;

import com.example.demo.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 중복 예약 방지용: 특정 회원 + 특정 스케줄 예약 여부 확인
    boolean existsByMemberIdAndScheduleId(Long memberId, Long scheduleId);
    
    // 관리자용: 모든 예약 조회 (연관 엔티티 포함)
    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.member " +
           "JOIN FETCH r.schedule s " +
           "JOIN FETCH s.program " +
           "JOIN FETCH r.ticket t " +
           "JOIN FETCH t.product " +
           "ORDER BY r.regDt DESC")
    List<Reservation> findAllWithDetails();
    
    // 관리자용: 특정 날짜 예약 조회
    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.member " +
           "JOIN FETCH r.schedule s " +
           "JOIN FETCH s.program " +
           "JOIN FETCH r.ticket t " +
           "JOIN FETCH t.product " +
           "WHERE s.startTime >= :startDate AND s.startTime < :endDate " +
           "ORDER BY s.startTime")
    List<Reservation> findByDateWithDetails(@Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);
}