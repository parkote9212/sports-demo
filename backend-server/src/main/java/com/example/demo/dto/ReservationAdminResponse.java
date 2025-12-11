package com.example.demo.dto;

import com.example.demo.domain.reservation.Reservation;
import com.example.demo.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationAdminResponse(
        Long reservationId,
        String memberName,
        String memberEmail,
        String scheduleName,
        LocalDateTime scheduleDateTime,
        String ticketName,
        ReservationStatus status,
        LocalDateTime reservedAt
) {
    public static ReservationAdminResponse from(Reservation reservation) {
        return new ReservationAdminResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getMember().getEmail(),
                reservation.getSchedule().getProgram().getName(),
                reservation.getSchedule().getStartTime(),
                reservation.getTicket().getProduct().getName(),
                reservation.getStatus(),
                reservation.getRegDt()
        );
    }
}