package com.djokic.canteenreservationsystem.dto.status;

import com.djokic.canteenreservationsystem.enumeration.MealTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record SlotStatusResponse(
        LocalDate date,
        MealTypeEnum meal,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        int remainingCapacity
) {}
