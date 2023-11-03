package com.parkeer.parkeer.dto.vehicle;

import com.parkeer.parkeer.entity.vehicle.VehicleType;

public record VehicleDTO(
        Long userId,
        String plate,
        String branch,
        String model,
        Integer year,
        VehicleType type
) {
}
