package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ScheduleAdminDto(
        Long scheduleId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 화면에 예쁘게 표시
        LocalDateTime startTime,

        @JsonFormat(pattern = "HH:mm")
        LocalDateTime endTime,

        String branchName,      // 지점명 (강남점)
        String programName,     // 종목명 (GX, PT)
        String instructorName,  // 강사명 (김헬스)

        int currentCount,       // 현재 예약 인원
        int maxCount,           // 정원
        String status           // 상태 (OPEN, FULL, CLOSE)
) {}