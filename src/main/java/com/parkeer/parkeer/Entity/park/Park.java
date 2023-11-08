package com.parkeer.parkeer.entity.park;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;

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
    private Long userId;
    private String plate;
    private int version;
    private Time time;
    private Status status;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdate;

    public Park(final ParkRedis parkRedis) {
        this.plate = parkRedis.getPlate();
        this.userId = parkRedis.getUserId();
        this.version = parkRedis.getVersion();
        this.time = parkRedis.getTime();
        this.status = parkRedis.getStatus();
        this.creationDate = LocalDateTime.parse(parkRedis.getCreationDate());
        this.lastUpdate = LocalDateTime.parse(parkRedis.getLastUpdate());
    }

}
