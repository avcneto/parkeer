package com.parkeer.parkeer.entity.receipt;

import com.parkeer.parkeer.entity.park.Time;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "tb_receipt")
public class Receipt {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private String plate;
    private Time time;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdate;
    private Integer duration;
    private BigDecimal minuteRate;
    private BigDecimal total;

    public Receipt(Long userId, String plate, Time time, LocalDateTime creationDate, LocalDateTime lastUpdate, Integer duration, BigDecimal minuteRate, BigDecimal total) {
        this.userId = userId;
        this.plate = plate;
        this.time = time;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
        this.duration = duration;
        this.minuteRate = minuteRate;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "userId=" + userId +
                ", plate='" + plate + '\'' +
                ", time=" + time +
                ", creationDate=" + creationDate +
                ", lastUpdate=" + lastUpdate +
                ", duration=" + duration +
                ", minuteRate=" + minuteRate +
                ", total=" + total +
                '}';
    }
}
