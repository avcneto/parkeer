package com.parkeer.parkeer.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return Timestamp.from(zonedDateTime.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant().atZone(ZoneId.systemDefault());
    }
}