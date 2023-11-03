package com.parkeer.parkeer.entity.park;

import com.parkeer.parkeer.util.ZonedDateTimeConverter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "tb_park")
public class Park {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String plate;
    private int version;
    private Time time;
    private Status status;
    @Column(name = "creation_Date", columnDefinition = "TIMESTAMP")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime creationDate;
    @Column(name = "last_update", columnDefinition = "TIMESTAMP")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime lastUpdate;

    public Park(final ParkRedis parkRedis) {
        this.plate = parkRedis.getPlate();
        this.version = parkRedis.getVersion();
        this.time = parkRedis.getTime();
        this.status = parkRedis.getStatus();
        this.creationDate = ZonedDateTime.parse(parkRedis.getCreationDate());
        this.lastUpdate = ZonedDateTime.parse(parkRedis.getLastUpdate());
    }
}
