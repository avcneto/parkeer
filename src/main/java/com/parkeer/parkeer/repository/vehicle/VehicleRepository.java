package com.parkeer.parkeer.repository.vehicle;

import com.parkeer.parkeer.dto.vehicle.UserPlateDTO;
import com.parkeer.parkeer.entity.vehicle.Vehicle;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface VehicleRepository extends R2dbcRepository<Vehicle, Long> {

    Flux<Vehicle> findByIdOrUserIdOrPlate(Long id, Long userId, String plate);

    @Query(value = """
            SELECT
                user.id AS user_id,
                vehicle.plate AS plate
            FROM tb_user AS user
            LEFT JOIN tb_vehicle AS vehicle ON user.id = vehicle.user_id
            WHERE user.id = :userId;
            """)
    Flux<UserPlateDTO> findUserWithVehicle(@Param("userId") Long userId);
}
