package com.example.demo.domain.schedule;

import com.example.demo.domain.staff.Staff;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULE")
@Getter
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLS_ID")
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID")
    private Staff staff; // 담당 강사

    @Column(name = "STRT_DT")
    private LocalDateTime startTime;

    @Column(name = "END_DT")
    private LocalDateTime endTime;

    @Column(name = "MAX_NOP_CNT")
    private int maxCount;

    @Column(name = "RSV_CNT")
    private int currentCount;

    @Column(name = "STTS_CD")
    @Enumerated(EnumType.STRING) // [수정] String -> Enum 매핑
    private ScheduleStatus status;

    // 필드 아래에 추가
    public void increaseCurrentCount() {
        if (this.currentCount >= this.maxCount) {
            throw new IllegalStateException("정원 초과");
        }
        this.currentCount++;

        // 정원이 꽉 차면 상태를 FULL(Enum)로 변경
        if (this.currentCount == this.maxCount) {
            this.status = ScheduleStatus.FULL;
        }
    }
}