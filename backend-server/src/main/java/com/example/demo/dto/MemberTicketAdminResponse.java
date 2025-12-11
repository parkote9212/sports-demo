package com.example.demo.dto;

import com.example.demo.domain.ticket.MemberTicket;
import com.example.demo.domain.ticket.TicketStatus;

import java.time.LocalDate;

public record MemberTicketAdminResponse(
        Long ticketId,
        String memberName,
        String memberEmail,
        String productName,
        int remainingCount,
        TicketStatus status,
        LocalDate startDate,
        LocalDate endDate
) {
    public static MemberTicketAdminResponse from(MemberTicket ticket) {
        return new MemberTicketAdminResponse(
                ticket.getId(),
                ticket.getMember().getName(),
                ticket.getMember().getEmail(),
                ticket.getProduct().getName(),
                ticket.getRemainingCount(),
                ticket.getStatus(),
                ticket.getStartDate(),
                ticket.getEndDate()
        );
    }
}