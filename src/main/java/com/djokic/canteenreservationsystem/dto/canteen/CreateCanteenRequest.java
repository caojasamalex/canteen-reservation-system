package com.djokic.canteenreservationsystem.dto.canteen;

import java.util.List;

public record CreateCanteenRequest(
        String name,
        String location,
        int capacity,
        List<WorkingHourRequest> workingHours
) {}
