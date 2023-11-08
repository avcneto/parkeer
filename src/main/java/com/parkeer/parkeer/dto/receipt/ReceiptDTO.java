package com.parkeer.parkeer.dto.receipt;

import com.parkeer.parkeer.entity.park.Time;
import com.parkeer.parkeer.entity.receipt.Receipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReceiptDTO {
    private Long id;
    private Long userId;
    private String plate;
    private Time time;
    private String creationDate;
    private String lastUpdate;
    private Integer duration;
    private BigDecimal minuteRate;
    private BigDecimal total;

    public ReceiptDTO(Receipt receipt) {
        this.id = receipt.getId();
        this.userId = receipt.getUserId();
        this.plate = receipt.getPlate();
        this.time = receipt.getTime();
        this.creationDate = receipt.getCreationDate().toString();
        this.lastUpdate = receipt.getLastUpdate().toString();
        this.duration = receipt.getDuration();
        this.minuteRate = receipt.getMinuteRate();
        this.total = receipt.getTotal();
    }
}
