package com.djokic.canteenreservationsystem.dto.status;

import com.djokic.canteenreservationsystem.enumeration.MealTypeEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public record SlotStatusResponse(
        LocalDate date,
        MealTypeEnum meal,
        LocalTime startTime,
        int remainingCapacity
) {}
