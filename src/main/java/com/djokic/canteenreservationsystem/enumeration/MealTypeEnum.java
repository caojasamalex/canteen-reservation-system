package com.djokic.canteenreservationsystem.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MealTypeEnum {
    @JsonProperty("breakfast")
    BREAKFAST,

    @JsonProperty("lunch")
    LUNCH,

    @JsonProperty("dinner")
    DINNER
}
