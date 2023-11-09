package com.parkeer.parkeer.util;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.parkeer.parkeer.config.SchedulingConfig.PRICE_POR_MINUTE_PARKED;

public class Validators {
    private static final Integer TIME_EXPIRED = 5;
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static Boolean hasTimeExpired(LocalDateTime initialLocalDateTime, LocalDateTime finalLocalDateTime) {
        return initialLocalDateTime.isAfter(finalLocalDateTime);
    }

    public static Boolean isNearTimeExpired(LocalDateTime initialLocalDateTime, LocalDateTime finalLocalDateTime) {
        return initialLocalDateTime.isAfter(finalLocalDateTime.minusMinutes(TIME_EXPIRED));
    }

    public static Integer getDuration(LocalDateTime creationDate, LocalDateTime lastUpdate) {
        return (int) Duration.between(creationDate, lastUpdate).toMinutes();
    }

    public static BigDecimal getPriceTotalByMinute(Integer duration) {
        return PRICE_POR_MINUTE_PARKED.multiply(new BigDecimal(duration));
    }
}
