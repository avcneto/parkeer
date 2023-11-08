package com.parkeer.parkeer.dto.park;

import com.parkeer.parkeer.entity.park.Status;

public record VehicleParkDTO(
        Long userId,
        String plate,
        String parkId,
        Status parkStatus,
        Integer parkVersion
) {
}
