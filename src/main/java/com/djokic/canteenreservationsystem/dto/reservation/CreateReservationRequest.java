package com.djokic.canteenreservationsystem.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservationRequest(
        Long studentId,
        Long canteenId,
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        int duration
) {}
