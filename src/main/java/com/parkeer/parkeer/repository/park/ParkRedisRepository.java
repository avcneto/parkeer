package com.parkeer.parkeer.repository.park;

import com.parkeer.parkeer.entity.park.ParkRedis;
import com.parkeer.parkeer.entity.park.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParkRedisRepository {

    Mono<Boolean> existsByPlate(String plate);

    Mono<ParkRedis> save(ParkRedis parkRedis);

    Mono<ParkRedis> findByPlate(String plate);

    Flux<ParkRedis> findAll();

    Mono<Long> delete(String plate);

    Flux<Long> deleteAll(Flux<String> keys);

    Flux<ParkRedis> findByStatus(Status status);

    Flux<ParkRedis> findByPlateAndStatus(String plate, Status status);

}
