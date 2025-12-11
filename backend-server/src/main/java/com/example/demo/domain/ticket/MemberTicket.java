package com.example.demo.domain.ticket;

import com.example.demo.domain.common.BaseTimeEntity;
import com.example.demo.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "MEMBER_TICKET")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTicket { // BaseTimeEntity 상속 X (테이블에 REG_DT 없음)

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TKT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROD_ID")
    private TicketProduct product;

    @Column(name = "RMN_CNT")
    private int remainingCount;

    @Column(name = "STTS_CD")
    @Enumerated(EnumType.STRING) // ACT, EXP, EXH
    private TicketStatus status;

    @Column(name = "STRT_DT")
    private LocalDate startDate;

    @Column(name = "END_DT")
    private LocalDate endDate;

    @Builder
    public MemberTicket(Member member, TicketProduct product, int remainingCount, TicketStatus status, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.product = product;
        this.remainingCount = remainingCount;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 필드 아래에 추가
    public void useTicket() {
        if (this.remainingCount <= 0) {
            throw new IllegalStateException("잔여 횟수 부족");
        }
        this.remainingCount--;
        if (this.remainingCount == 0) {
            this.status = TicketStatus.EXH; // 소진 처리
        }
    }
}