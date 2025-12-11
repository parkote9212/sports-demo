package com.example.demo.dto;

import com.example.demo.domain.ticket.ProgramType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TicketIssueRequest(
        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId,

        @NotNull(message = "이용권 종류는 필수입니다.")
        ProgramType type,  // PT, GX ...

        @NotNull(message = "횟수는 필수입니다.")
        Integer count,

        @NotNull(message = "시작일은 필수입니다.")
        LocalDate startDate,

        @NotNull(message = "종료일은 필수입니다.")
        LocalDate endDate
) {}