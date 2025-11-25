package com.djokic.canteenreservationsystem.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ReservationStatusEnum {
    @JsonProperty("Active")
    ACTIVE,

    @JsonProperty("Cancelled")
    CANCELLED
}
