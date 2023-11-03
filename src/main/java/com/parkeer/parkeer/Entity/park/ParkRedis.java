package com.parkeer.parkeer.entity.park;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@RedisHash("park")
public class ParkRedis implements Serializable {

    @Id
    @Indexed
    private String Plate;

    @Version
    private int version;
    private Time time;
    private Status status;
    private String creationDate = ZonedDateTime.now().toString();
    private String lastUpdate;
}
