package com.parkeer.parkeer.handler.vehicle;

import com.parkeer.parkeer.dto.vehicle.VehicleDTO;
import com.parkeer.parkeer.service.vehicle.VehicleService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.lang.String.format;

@Component
public record VehicleHandler(
        VehicleService vehicleService
) {

    private static final String VEHICLE_ID = "/vehicle/%s";

    public Mono<ServerResponse> addVehicle(final ServerRequest request) {
        return request
                .bodyToMono(VehicleDTO.class)
                .flatMap(vehicleService::addVehicle)
                .flatMap(vehicle -> ServerResponse
                        .created(URI.create(format(VEHICLE_ID, vehicle.getId())))
                        .bodyValue(vehicle))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getVehicleByIdOrUserIdOrPlate(final ServerRequest request) {
        return vehicleService.getVehicleByIdOrUserIdOrPlate(request.queryParams())
                .collectList()
                .flatMap(vehicle -> ServerResponse.ok().bodyValue(vehicle))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
