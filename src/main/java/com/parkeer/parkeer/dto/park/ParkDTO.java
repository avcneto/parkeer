package com.parkeer.parkeer.dto.park;

import com.parkeer.parkeer.entity.park.Status;
import com.parkeer.parkeer.entity.park.Time;

public record ParkDTO(
        String plate,
        Time time,
        Status status
) {
}
