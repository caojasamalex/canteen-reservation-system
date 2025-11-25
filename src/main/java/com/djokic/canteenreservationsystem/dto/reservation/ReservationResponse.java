package com.djokic.canteenreservationsystem.dto.reservation;

import com.djokic.canteenreservationsystem.enumeration.ReservationStatusEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
        Long id,
        ReservationStatusEnum status,
        Long studentId,
        Long canteenId,
        LocalDate date,
        LocalTime time,
        int duration
) {}
