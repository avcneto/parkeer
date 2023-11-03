package com.parkeer.parkeer.service.vehicle;

import com.parkeer.parkeer.dto.vehicle.UserPlateDTO;
import com.parkeer.parkeer.dto.vehicle.VehicleDTO;
import com.parkeer.parkeer.entity.vehicle.Vehicle;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.exception.ResourceAlreadyExistsException;
import com.parkeer.parkeer.repository.vehicle.VehicleRepository;
import com.parkeer.parkeer.util.QueryParams;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehicleService {

    private static final String VEHICLE_ALREADY_REGISTERED = "vehicle already registered for this user";
    private static final String UNREGISTERED_USER = "unregistered user";
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Mono<Vehicle> addVehicle(VehicleDTO vehicleDTO) {
        return findUserWithVehicle(vehicleDTO.userId())
                .switchIfEmpty(Mono.error(new BadRequestException(UNREGISTERED_USER)))
                .flatMap(it -> {
                    if (vehicleDTO.plate().equalsIgnoreCase(it.plate())) {
                        return Mono.error(new ResourceAlreadyExistsException(VEHICLE_ALREADY_REGISTERED));
                    }

                    return Mono.empty();
                })
                .onErrorResume(ResourceAlreadyExistsException.class, Mono::error)
                .then(vehicleRepository.save(new Vehicle(vehicleDTO)));

    }

    private Flux<UserPlateDTO> findUserWithVehicle(Long userId) {
        return vehicleRepository.findUserWithVehicle(userId);
    }


    public Flux<Vehicle> getVehicleByIdOrUserIdOrPlate(final MultiValueMap<String, String> params) {
        return Flux
                .just(params)
                .flatMap(it -> {
                    var query = new QueryParams(it);
                    query.validateIdAndUserIdAndPlate();

                    return vehicleRepository
                            .findByIdOrUserIdOrPlate(query.getIdOrNull(), query.getUserId(), query.getPlate());
                });
    }
}
