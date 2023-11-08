package com.parkeer.parkeer.dto.payment_method;

import com.parkeer.parkeer.entity.payment_method.PaymentMethodType;

public record PaymentUpdateMethodDTO(
        Long userId,
        PaymentMethodType paymentMethodType,
        String pixKey,
        String cardNumber,
        String cardFlag
) {
}
