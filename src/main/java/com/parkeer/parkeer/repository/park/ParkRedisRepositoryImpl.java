package com.parkeer.parkeer.repository.park;

import com.parkeer.parkeer.entity.park.ParkRedis;
import com.parkeer.parkeer.entity.park.Status;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.exception.ResourceAlreadyExistsException;
import com.parkeer.parkeer.exception.ResourceAlreadyUpdatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Slf4j
@Repository
public class ParkRedisRepositoryImpl implements ParkRedisRepository {

    private final static String KEY = "PARK";
    private final static String ALREADY_UPDATED = "This record has already been updated earlier by another object.";
    private final static String DUPLICATE_MESSAGE = "Duplicate key, plate: %s exists.";
    private final static String PLATE_IS_NULL = "plate is null";
    private final static String CANNOT_BE_SAVED = "Cannot be saved: plate are required, but one or both is empty.";

    private final ReactiveHashOperations<String, String, ParkRedis> hashOperations;

    @Autowired
    public ParkRedisRepositoryImpl(ReactiveRedisOperations<String, ParkRedis> redisOperations) {
        this.hashOperations = redisOperations.opsForHash();
    }

    @Override
    public Mono<ParkRedis> findByPlate(String plate) {
       return hashOperations.get(KEY, plate);
    }

    @Override
    public Flux<ParkRedis> findAll() {
        return hashOperations.values(KEY);
    }

    @Override
    public Mono<Long> delete(String id) {
        return hashOperations.remove(KEY, id);
    }

    @Override
    public Flux<Long> deleteAll(Flux<String> ids) {
        return ids.flatMap(this::delete);
    }

    @Override
    public Mono<Boolean> existsByPlate(String plate) {
        return findByPlate(plate).hasElement();
    }

    @Override
    public Mono<ParkRedis> save(ParkRedis parkRedis) {
        if (parkRedis.getPlate() == null) {
            return Mono.error(new BadRequestException(PLATE_IS_NULL));
        }

        if (parkRedis.getPlate().isEmpty() || parkRedis.getPlate().isBlank()) {
            return Mono.error(new BadRequestException(CANNOT_BE_SAVED))
                    .thenReturn(parkRedis);
        }

        return findByPlate(parkRedis.getPlate())
                .flatMap(it -> {
                    if (it.getVersion() != parkRedis.getVersion()) {
                        return Mono.error(new ResourceAlreadyUpdatedException(ALREADY_UPDATED));
                    } else {
                        parkRedis.setVersion(parkRedis.getVersion() + 1);

                        return Mono.defer(() -> {
                            Mono<Boolean> exists = Mono.just(false);

                            if (!it.getPlate().equals(parkRedis.getPlate())) {
                                exists = existsByPlate(parkRedis.getPlate());
                            }

                            return addOrUpdateUser(parkRedis, exists);
                        });
                    }
                })
                .switchIfEmpty(Mono.defer(() -> addOrUpdateUser(parkRedis, existsByPlate(parkRedis.getPlate())
                        .mergeWith(existsByPlate(parkRedis.getPlate()))
                        .any(it -> it))));

    }

    private Mono<ParkRedis> addOrUpdateUser(ParkRedis parkRedis, Mono<Boolean> exists) {
        return exists.flatMap(exist -> {
                    if (exist) {
                        return Mono.error(new ResourceAlreadyExistsException(
                                format(DUPLICATE_MESSAGE, parkRedis.getPlate())));
                    } else {
                        return hashOperations.put(KEY, parkRedis.getPlate(), parkRedis)
                                .map(isSaved -> parkRedis);
                    }
                })
                .thenReturn(parkRedis);
    }
}
