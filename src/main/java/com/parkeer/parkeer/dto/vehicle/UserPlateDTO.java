package com.parkeer.parkeer.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;

public record UserPlateDTO(
        Long userId,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        @Pattern(regexp = PLATE_REGEX, message = PLATE_INVALID_MESSAGE)
        String plate
) {
    private static final String FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE = "Field cannot be null, empty or blank";
    private static final String PLATE_INVALID_MESSAGE = "Plate is invalid, example: 000-00000";
    private static final String PLATE_REGEX = "\\b[A-Za-z0-9]{3}-[A-Za-z0-9]{5}\\b";
}
