package com.djokic.canteenreservationsystem.dto.canteen;

import com.djokic.canteenreservationsystem.enumeration.MealTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record WorkingHourRequest(
        MealTypeEnum meal,

        @JsonFormat(pattern = "HH:mm")
        LocalTime from,

        @JsonFormat(pattern = "HH:mm")
        LocalTime to
) {}
