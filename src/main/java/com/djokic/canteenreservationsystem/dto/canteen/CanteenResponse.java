package com.djokic.canteenreservationsystem.dto.canteen;

import java.util.List;

public record CanteenResponse(
        Long id,
        String name,
        String location,
        int capacity,
        List<WorkingHourResponse> workingHours
) {}
