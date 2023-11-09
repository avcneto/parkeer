package com.parkeer.parkeer.dto.park;

import com.parkeer.parkeer.entity.park.Status;
import com.parkeer.parkeer.entity.park.Time;

public record VehicleParkDTO(
        Long userId,
        String plate,
        String parkId,
        Status parkStatus,
        Integer parkVersion
) {
}
