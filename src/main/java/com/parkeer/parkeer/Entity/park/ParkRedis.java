package com.parkeer.parkeer.entity.park;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@RedisHash("park")
public class ParkRedis implements Serializable {

    @Id
    @Indexed
    private String Plate;
    private Long userId;

    @Version
    private int version;
    private Time time;

    @Indexed
    private Status status;
    private String creationDate = LocalDateTime.now().toString();
    private String lastUpdate;

    public ParkRedis(Park park) {
        Plate = park.getPlate();
        this.userId = park.getUserId();
        this.version = park.getVersion();
        this.time = park.getTime();
        this.status = park.getStatus();
        this.creationDate = park.getCreationDate().toString();
        this.lastUpdate = park.getLastUpdate().toString();
    }
}
