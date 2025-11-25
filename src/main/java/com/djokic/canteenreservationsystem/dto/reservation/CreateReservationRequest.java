package com.djokic.canteenreservationsystem.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservationRequest(
        Long studentId,
        Long canteenId,
        LocalDate date,
        LocalTime time,
        int duration
) {}
