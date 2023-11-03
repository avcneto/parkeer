package com.parkeer.parkeer.repository.park;

import com.parkeer.parkeer.dto.park.VehicleParkDTO;
import com.parkeer.parkeer.entity.park.Park;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface ParkRepository extends R2dbcRepository<Park, Long> {

    @Query(value = """
            SELECT
                vehicle.user_id AS user_Id,
                vehicle.plate AS plate,
                park.id AS park_id,
                park.status AS park_status
            FROM tb_vehicle AS vehicle
            LEFT JOIN tb_park AS park on park.plate = vehicle.plate
            WHERE vehicle.plate = :plate;
            """)
    Flux<VehicleParkDTO> findVehicleByPlateAndParkPlate(@Param("plate") String plate);
}
