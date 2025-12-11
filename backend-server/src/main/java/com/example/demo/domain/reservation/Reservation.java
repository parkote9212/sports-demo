package com.example.demo.domain.reservation;

import com.example.demo.domain.common.BaseTimeEntity;
import com.example.demo.domain.member.Member;
import com.example.demo.domain.schedule.Schedule;
import com.example.demo.domain.ticket.MemberTicket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESERVATION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RSV_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHD_ID")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TKT_ID")
    private MemberTicket ticket; // 어떤 이용권을 사용했는지

    @Column(name = "STTS_CD")
    @Enumerated(EnumType.STRING) // RSV, CNCL, ATD
    private ReservationStatus status;

    @Builder
    public Reservation(Member member, Schedule schedule, MemberTicket ticket, ReservationStatus status) {
        this.member = member;
        this.schedule = schedule;
        this.ticket = ticket;
        this.status = status;
    }
}