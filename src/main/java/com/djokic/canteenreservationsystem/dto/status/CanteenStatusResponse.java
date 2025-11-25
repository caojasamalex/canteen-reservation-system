package com.djokic.canteenreservationsystem.dto.status;

import java.util.List;

public record CanteenStatusResponse(
        Long canteenId,
        List<SlotStatusResponse> slots
) {}
