package com.parkeer.parkeer.service.park;

import com.parkeer.parkeer.dto.park.ParkDTO;
import com.parkeer.parkeer.dto.park.UnparkDTO;
import com.parkeer.parkeer.entity.park.Park;
import com.parkeer.parkeer.entity.park.ParkRedis;
import com.parkeer.parkeer.entity.park.Status;
import com.parkeer.parkeer.entity.park.Time;
import com.parkeer.parkeer.entity.payment_method.PaymentMethodType;
import com.parkeer.parkeer.entity.receipt.Receipt;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.exception.NotFoundException;
import com.parkeer.parkeer.repository.park.ParkRedisRepositoryImpl;
import com.parkeer.parkeer.repository.park.ParkRepository;
import com.parkeer.parkeer.repository.payment_method.PaymentMethodRepository;
import com.parkeer.parkeer.repository.receipt.ReceiptRepository;
import com.parkeer.parkeer.util.QueryParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.parkeer.parkeer.config.SchedulingConfig.PRICE_POR_MINUTE_PARKED;
import static com.parkeer.parkeer.util.Producer.producerMessageConsole;
import static com.parkeer.parkeer.util.Validators.getDuration;
import static com.parkeer.parkeer.util.Validators.getPriceTotalByMinute;

@Slf4j
@Service
public class ParkService {

    private static final String VEHICLE_IS_PARKED = "vehicle is parked, please unpark the vehicle and try again";
    private static final String VEHICLE_UNREGISTERED = "unregistered vehicle";
    private static final String VEHICLE_IS_NOT_PARKED = "vehicle is not parked";
    private static final String YOUR_TICKET_IS_ALREADY = "Your ticket is already: %s";
    private static final String UNREGISTERED_PAYMENT_METHOD = "unregistered payment method";
    private static final String NOT_SEND_QUERY_PARAM = "plate or status not sent as query param";
    private static final String METHOD_NOT_ALLOWED = "payment method not allowed to this period of time";
    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;

    private final ReceiptRepository receiptRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ParkRedisRepositoryImpl parkRedisRepository;
    private final ParkRepository parkRepository;

    public ParkService(ReceiptRepository receiptRepository, PaymentMethodRepository paymentMethodRepository, ParkRedisRepositoryImpl parkRedisRepository, ParkRepository parkRepository) {
        this.receiptRepository = receiptRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.parkRedisRepository = parkRedisRepository;
        this.parkRepository = parkRepository;
    }

    public Mono<ParkRedis> park(final ParkDTO parkDTO) {
        return parkRedisRepository.findByPlate(parkDTO.plate())
                .flatMap(park -> {
                    if (Status.START.equals(park.getStatus())) {
                        return Mono.error(new BadRequestException(VEHICLE_IS_PARKED));
                    } else {
                        return paymentMethodRepository.findByUserId(park.getUserId())
                                .flatMap(paymentMethod -> {
                                    if (PaymentMethodType.PIX.equals(paymentMethod.getPaymentMethodType()) && Time.INDETERMINATE.equals(park.getTime())) {
                                        return Mono.error(new BadRequestException(METHOD_NOT_ALLOWED));
                                    }

                                    return saveParkRedis(parkDTO, park.getVersion());
                                })
                                .switchIfEmpty(Mono.error(new BadRequestException(UNREGISTERED_PAYMENT_METHOD)));
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

                                    return paymentMethodRepository.findByUserId(parkDTO.userId())
                                            .flatMap(paymentMethod -> {
                                                if (PaymentMethodType.PIX.equals(paymentMethod.getPaymentMethodType()) && Time.INDETERMINATE.equals(parkDTO.time())) {
                                                    return Mono.error(new BadRequestException(METHOD_NOT_ALLOWED));
                                                }

                                                return saveParkRedis(parkDTO, ZERO);
                                            })
                                            .switchIfEmpty(Mono.error(new BadRequestException(UNREGISTERED_PAYMENT_METHOD)));
                                })
                )
                .onErrorResume(BadRequestException.class, Mono::error);
    }

    private Mono<ParkRedis> saveParkRedis(ParkDTO parkDTO, int version) {
        var now = LocalDateTime.now();
        var parkRedis = new ParkRedis(
                parkDTO.plate(),
                parkDTO.userId(),
                version,
                parkDTO.time(),
                Status.START,
                now.toString(),
                getLastUpdate(parkDTO.time(), now).toString()
        );

        return this.parkRedisRepository.save(parkRedis);
    }

    private LocalDateTime getLastUpdate(Time time, LocalDateTime now) {
        return switch (time) {
            case ONE_MINUTE -> now.plusMinutes(1);
            case FIFTEEN -> now.plusMinutes(15);
            case THIRTY -> now.plusMinutes(30);
            case SIXTY -> now.plusHours(1);
            default -> now.plusDays(31);
        };
    }

    public Flux<ParkRedis> unPark(final UnparkDTO unparkDTO) {
        return Flux.just(unparkDTO)
                .flatMap(unpark -> parkRedisRepository.findByPlate(unpark.plate())
                        .flatMap(it -> {
                            if (Status.END.equals(it.getStatus())) {
                                return Mono.error(new NotFoundException(VEHICLE_IS_NOT_PARKED));
                            } else {
                                it.setStatus(Status.END);
                                it.setVersion(it.getVersion() + ONE);

                                return parkRepository.save(new Park(it))
                                        .flatMap(savedPark -> {
                                            var duration = getDuration(savedPark.getCreationDate(), savedPark.getLastUpdate());
                                            var receipt = new Receipt(
                                                    savedPark.getUserId(),
                                                    savedPark.getPlate(),
                                                    savedPark.getTime(),
                                                    savedPark.getCreationDate(),
                                                    savedPark.getLastUpdate(),
                                                    duration,
                                                    PRICE_POR_MINUTE_PARKED,
                                                    getPriceTotalByMinute(duration)
                                            );

                                            producerMessageConsole(receipt, YOUR_TICKET_IS_ALREADY);

                                            return parkRedisRepository
                                                    .delete(savedPark.getPlate())
                                                    .then(receiptRepository.save(receipt))
                                                    .thenReturn(savedPark);
                                        })
                                        .map(ParkRedis::new);
                            }
                        })
                        .flux()
                        .switchIfEmpty(
                                parkRepository.findByPlateAndStatus(unparkDTO.plate(), Status.START)
                                        .flatMap(it2 -> {

                                            it2.setVersion(it2.getVersion() + ONE);
                                            it2.setStatus(Status.END);

                                            var duration = getDuration(it2.getCreationDate(), it2.getLastUpdate());
                                            var receipt = new Receipt(
                                                    it2.getUserId(),
                                                    it2.getPlate(),
                                                    it2.getTime(),
                                                    it2.getCreationDate(),
                                                    it2.getLastUpdate(),
                                                    duration,
                                                    PRICE_POR_MINUTE_PARKED,
                                                    getPriceTotalByMinute(duration)
                                            );

                                            producerMessageConsole(receipt, YOUR_TICKET_IS_ALREADY);

                                            return parkRepository.save(it2)
                                                    .flatMap(it -> receiptRepository.save(receipt)
                                                            .thenReturn(it));
                                        })
                                        .map(ParkRedis::new)
                        )
                        .switchIfEmpty(Mono.error(new NotFoundException(VEHICLE_IS_NOT_PARKED)))
                        .onErrorResume(NotFoundException.class, Mono::error)
                );
    }

    public Flux<ParkRedis> getParkByPlateAndStatus(final MultiValueMap<String, String> params) {
        return Flux
                .just(params)
                .flatMap(it -> {
                    var query = new QueryParams(it);

                    if (query.hasPlateOrStatus()) {
                        var parkRedis = parkRedisRepository.findByPlateAndStatus(query.getPlate(), query.getStatus());
                        var parkMysql = parkRepository.findByPlateAndStatus(query.getPlate(), query.getStatus()).map(ParkRedis::new);

                        return parkRedis.concatWith(parkMysql);
                    }

                    if (query.hasPlate()) {
                        var parkRedis = parkRedisRepository.findByPlate(query.getPlate());
                        var parkMysql = parkRepository.findByPlate(query.getPlate()).map(ParkRedis::new);

                        return parkRedis.concatWith(parkMysql);

                    }

                    if (query.hasStatus()) {
                        var parkRedis = parkRedisRepository.findByStatus(query.getStatus());
                        var parkMysql = parkRepository.findByStatus(query.getStatus()).map(ParkRedis::new);

                        return parkRedis.concatWith(parkMysql);
                    }

                    return Mono.error(new BadRequestException(NOT_SEND_QUERY_PARAM));
                });
    }
}
