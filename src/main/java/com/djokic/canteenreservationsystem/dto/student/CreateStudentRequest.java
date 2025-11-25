package com.djokic.canteenreservationsystem.dto.student;

public record CreateStudentRequest(
        String name,
        String email,
        boolean isAdmin
) {}
