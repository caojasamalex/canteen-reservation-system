package com.djokic.canteenreservationsystem.dto.student;

public record StudentResponse(
        Long id,
        String name,
        String email,
        boolean isAdmin
) {}
