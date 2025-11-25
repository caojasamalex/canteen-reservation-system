package com.djokic.canteenreservationsystem.dto.canteen;

import com.djokic.canteenreservationsystem.enumeration.MealTypeEnum;

import java.time.LocalTime;

public record WorkingHourResponse(
        MealTypeEnum meal,
        LocalTime from,
        LocalTime to
) {}
