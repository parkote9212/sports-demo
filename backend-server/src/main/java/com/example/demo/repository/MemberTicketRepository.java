package com.example.demo.repository;

import com.example.demo.domain.ticket.MemberTicket;
import com.example.demo.domain.ticket.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface MemberTicketRepository extends JpaRepository<MemberTicket, Long> {
    // 특정 회원의 이용권 목록 조회 (최신순)
    List<MemberTicket> findByMemberIdOrderByIdDesc(Long memberId);

    Optional<MemberTicket> findFirstByMemberIdAndStatusAndRemainingCountGreaterThanOrderByStartDateAsc(
            Long memberId, TicketStatus status, int remainingCount
    );
    
    // 관리자용: 전체 회원 이용권 조회 (연관 엔티티 포함)
    @Query("SELECT mt FROM MemberTicket mt " +
           "JOIN FETCH mt.member " +
           "JOIN FETCH mt.product " +
           "ORDER BY mt.id DESC")
    List<MemberTicket> findAllWithDetails();
}