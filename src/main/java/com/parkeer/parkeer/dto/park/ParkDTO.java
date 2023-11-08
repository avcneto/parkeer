package com.parkeer.parkeer.dto.park;

import com.parkeer.parkeer.entity.park.Time;

public record ParkDTO(
        Long userId,
        String plate,
        Time time
) {
}
