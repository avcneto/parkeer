package com.parkeer.parkeer.service.park;

import com.parkeer.parkeer.dto.park.ParkDTO;
import com.parkeer.parkeer.entity.park.Park;
import com.parkeer.parkeer.entity.park.ParkRedis;
import com.parkeer.parkeer.entity.park.Status;
import com.parkeer.parkeer.entity.park.Time;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.repository.park.ParkRedisRepositoryImpl;
import com.parkeer.parkeer.repository.park.ParkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Slf4j
@Service
@EnableScheduling
public class ParkService {

    private static final String REDIS_SYNCHRONIZATION_EMPTY_MESSAGE = "redis empty, synchronization not performed.";
    private static final String SUCCESSFUL_SYNCHRONIZATION = "successful synchronization";
    private static final String VEHICLE_IS_PARKED = "vehicle is parked, please unpark the vehicle and try again";
    private static final String VEHICLE_UNREGISTERED = "unregistered vehicle";
    private static final long TIME = 50000;
    private final ParkRedisRepositoryImpl parkRedisRepository;
    private final ParkRepository parkRepository;

    public ParkService(ParkRedisRepositoryImpl parkRedisRepository, ParkRepository parkRepository) {
        this.parkRedisRepository = parkRedisRepository;
        this.parkRepository = parkRepository;
    }

    public Mono<ParkRedis> park(final ParkDTO parkDTO) {
        return parkRedisRepository.findByPlate(parkDTO.plate())
                .flatMap(park -> {
                    if (Status.START.equals(park.getStatus())) {
                        return Mono.error(new BadRequestException(VEHICLE_IS_PARKED));
                    } else {
                        return saveParkRedis(parkDTO);
                    }
                })
                .switchIfEmpty(
                        parkRepository.findVehicleByPlateAndParkPlate(parkDTO.plate())
                                .collectList()
                                .flatMap(vehiclesParkDTO -> {
                                    var parkedVehicle = vehiclesParkDTO
                                            .stream()
                                            .filter(it -> Status.START.equals(it.parkStatus()))
                                            .findFirst();

                                    if (parkedVehicle.isPresent()) {
                                        return Mono.error(new BadRequestException(VEHICLE_IS_PARKED));
                                    }

                                    if (vehiclesParkDTO.isEmpty()) {
                                        return Mono.error(new BadRequestException(VEHICLE_UNREGISTERED));
                                    }

                                    return saveParkRedis(parkDTO);
                                })
                )
                .onErrorResume(BadRequestException.class, Mono::error);
    }

    private Mono<ParkRedis> saveParkRedis(ParkDTO parkDTO) {
        var now = ZonedDateTime.now();
        var parkRedis = new ParkRedis(
                parkDTO.plate(),
                0,
                parkDTO.time(),
                parkDTO.status(),
                now.toString(),
                getLastUpdate(parkDTO.time(), now).toString()
        );

        return this.parkRedisRepository.save(parkRedis);
    }

    private ZonedDateTime getLastUpdate(Time time, ZonedDateTime now) {
        return switch (time) {
            case FIFTEEN -> now.plusMinutes(15);
            case THIRTY -> now.plusMinutes(30);
            case SIXTY -> now.plusHours(1);
            default -> now.plusHours(2);
        };
    }

    @Scheduled(fixedDelay = TIME)
    public void synchronizeDatabase() {
        parkRedisRepository.findAll()
                .collectList()
                .flatMapMany(allRedisPark -> {

                    if (allRedisPark.isEmpty()) {
                        log.info(REDIS_SYNCHRONIZATION_EMPTY_MESSAGE);

                        return Mono.empty();
                    } else {
                        var saveAll = allRedisPark.stream().map(Park::new).toList();
                        var deleteAll = Flux.fromIterable(allRedisPark.stream().map(ParkRedis::getPlate).toList());

                        return parkRepository.saveAll(saveAll)
                                .thenMany(parkRedisRepository.deleteAll(deleteAll))
                                .doOnNext(it -> log.info(SUCCESSFUL_SYNCHRONIZATION))
                                .thenMany(Mono.empty());
                    }
                }).subscribe();
    }
}
