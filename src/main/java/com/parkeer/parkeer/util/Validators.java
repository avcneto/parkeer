package com.parkeer.parkeer.util;

import java.time.LocalDateTime;

public class Validators {
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static Boolean hasTimeExpired(LocalDateTime initialLocalDateTime, LocalDateTime finalLocalDateTime) {
        return initialLocalDateTime.isAfter(finalLocalDateTime);
    }
}
