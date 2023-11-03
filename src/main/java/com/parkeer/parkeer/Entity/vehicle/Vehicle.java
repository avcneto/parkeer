package com.parkeer.parkeer.entity.vehicle;

import com.parkeer.parkeer.dto.vehicle.VehicleDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_vehicle")
public class Vehicle {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private String plate;
    private String branch;
    private String model;
    private Integer year;
    private VehicleType type;

    public Vehicle(VehicleDTO vehicleDTO) {
        this.userId = vehicleDTO.userId();
        this.plate = vehicleDTO.plate();
        this.branch = vehicleDTO.branch();
        this.model = vehicleDTO.model();
        this.year = vehicleDTO.year();
        this.type = vehicleDTO.type();
    }
}
