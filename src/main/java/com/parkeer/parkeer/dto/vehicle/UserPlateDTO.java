package com.parkeer.parkeer.dto.vehicle;

import java.util.Optional;

public record UserPlateDTO(
        Long userId,
        String plate
) {
}
