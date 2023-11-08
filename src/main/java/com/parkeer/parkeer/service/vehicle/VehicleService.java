package com.parkeer.parkeer.service.vehicle;

import com.parkeer.parkeer.dto.vehicle.UserPlateDTO;
import com.parkeer.parkeer.dto.vehicle.VehicleDTO;
import com.parkeer.parkeer.dto.vehicle.VehicleUpdateDTO;
import com.parkeer.parkeer.entity.vehicle.Vehicle;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.exception.FailedDependencyException;
import com.parkeer.parkeer.exception.ResourceAlreadyExistsException;
import com.parkeer.parkeer.repository.vehicle.VehicleRepository;
import com.parkeer.parkeer.util.QueryParams;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static com.parkeer.parkeer.util.Validators.isNullOrBlank;

@Service
public class VehicleService {

    private static final String INACCESSIBLE_FIELDS = "user update has inaccessible fields";
    private static final String NON_MATCHING = "non-matching fields between UserUpdateDTO and User";
    private static final String VEHICLE_ALREADY_REGISTERED = "vehicle already registered for this user";
    private static final String UNREGISTERED_USER = "unregistered user";
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Mono<Vehicle> addVehicle(final VehicleDTO vehicleDTO) {
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

    public Mono<Void> deleteVehicleById(final MultiValueMap<String, String> params) {
        return Mono
                .just(params)
                .flatMap(it -> vehicleRepository.deleteById(new QueryParams(it).getIdOrBadRequestException()));
    }

    public Mono<Vehicle> updateVehicleById(final MultiValueMap<String, String> params, final VehicleUpdateDTO vehicleUpdateDTO) {
        return Mono
                .just(vehicleUpdateDTO)
                .flatMap(vehicleUpdate -> vehicleRepository
                        .findById(new QueryParams(params).getIdOrBadRequestException())
                        .flatMap(vehicle -> {
                            updateVehicleByVehicleUpdateDTO(vehicleUpdate, vehicle);
                            return vehicleRepository.save(vehicle);
                        })
                );
    }

    private void updateVehicleByVehicleUpdateDTO(final VehicleUpdateDTO vehicleUpdateDTO, Vehicle vehicle) {
        Field[] fields = VehicleUpdateDTO.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(vehicleUpdateDTO);
            } catch (IllegalAccessException ex) {
                throw new FailedDependencyException(INACCESSIBLE_FIELDS, ex);
            }

            if (value != null && !isNullOrBlank(value.toString())) {
                Field correspondingField;

                try {
                    correspondingField = Vehicle.class.getDeclaredField(field.getName());
                    correspondingField.setAccessible(true);

                    correspondingField.set(vehicle, value);

                } catch (NoSuchFieldException ex) {
                    throw new FailedDependencyException(NON_MATCHING, ex);
                } catch (IllegalAccessException ex) {
                    throw new FailedDependencyException(INACCESSIBLE_FIELDS, ex);
                }
            }
        }

    }
}
