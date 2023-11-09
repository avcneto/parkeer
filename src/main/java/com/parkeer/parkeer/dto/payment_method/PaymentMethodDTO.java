package com.parkeer.parkeer.dto.payment_method;

import com.parkeer.parkeer.entity.payment_method.PaymentMethodType;
import jakarta.validation.constraints.NotBlank;

public record PaymentMethodDTO(
        Long userId,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        PaymentMethodType paymentMethodType,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String pixKey,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String cardNumber,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String cardFlag
) {
    private static final String FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE = "Field cannot be null, empty or blank";
}
