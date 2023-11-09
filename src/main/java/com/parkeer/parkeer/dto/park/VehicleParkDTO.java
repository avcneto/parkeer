package com.parkeer.parkeer.dto.park;

import com.parkeer.parkeer.entity.park.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VehicleParkDTO(
        Long userId,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        @Pattern(regexp = PLATE_REGEX, message = PLATE_INVALID_MESSAGE)
        String plate,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String parkId,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        Status parkStatus,
        @NotNull(message = FIELD_CANNOT_BE_NULL_OR_EMPTY_MESSAGE)
        Integer parkVersion
) {
    private static final String FIELD_CANNOT_BE_NULL_OR_EMPTY_MESSAGE = "Field cannot be null or empty";
    private static final String FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE = "Field cannot be null, empty or blank";
    private static final String PLATE_INVALID_MESSAGE = "Plate is invalid, example: 000-00000";
    private static final String PLATE_REGEX = "\\b[A-Za-z0-9]{3}-[A-Za-z0-9]{5}\\b";
}
