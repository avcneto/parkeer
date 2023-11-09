package com.parkeer.parkeer.handler.vehicle;

import com.parkeer.parkeer.dto.vehicle.VehicleDTO;
import com.parkeer.parkeer.dto.vehicle.VehicleUpdateDTO;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.service.vehicle.VehicleService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;

@Component
public record VehicleHandler(
        VehicleService vehicleService,
        Validator validator
) {
    private static final String BODY_IS_EMPTY = "body is empty";
    private static final String VEHICLE_ID = "/vehicle/%s";

    public Mono<ServerResponse> addVehicle(ServerRequest request) {
        return request.bodyToMono(VehicleDTO.class)
                .flatMap(this::validateAndAddVehicle)
                .flatMap(vehicle -> ServerResponse
                        .created(URI.create(format(VEHICLE_ID, vehicle.getId())))
                        .bodyValue(vehicle))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage())
                );
    }

    private Mono<com.parkeer.parkeer.entity.vehicle.Vehicle> validateAndAddVehicle(VehicleDTO vehicleDTO) {
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validate(vehicleDTO);
        if (!violations.isEmpty()) {
            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return Mono.error(new BadRequestException("Validation Error: " + errorMessages));
        } else {
            return vehicleService.addVehicle(vehicleDTO);
        }
    }


    public Mono<ServerResponse> getVehicleByIdOrUserIdOrPlate(final ServerRequest request) {
        return vehicleService.getVehicleByIdOrUserIdOrPlate(request.queryParams())
                .collectList()
                .flatMap(vehicle -> ServerResponse.ok().bodyValue(vehicle))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteByVehicleId(final ServerRequest request) {
        return vehicleService.deleteVehicleById(request.queryParams())
                .then(Mono.defer(() -> noContent().build()));
    }

    public Mono<ServerResponse> updateVehicleById(final ServerRequest request) {
        return request
                .bodyToMono(VehicleUpdateDTO.class)
                .switchIfEmpty(Mono.error(new BadRequestException(BODY_IS_EMPTY)))
                .flatMap(userUpdateDTO -> vehicleService.updateVehicleById(request.queryParams(), userUpdateDTO))
                .flatMap(vehicle -> ServerResponse.ok().bodyValue(vehicle))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
