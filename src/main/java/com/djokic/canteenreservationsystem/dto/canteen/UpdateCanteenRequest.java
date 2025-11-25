package com.djokic.canteenreservationsystem.dto.canteen;

public record UpdateCanteenRequest(
        String name,
        String location,
        Integer capacity
) {}
