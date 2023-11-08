package com.parkeer.parkeer.repository.payment_method;

import com.parkeer.parkeer.entity.payment_method.PaymentMethod;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentMethodRepository extends R2dbcRepository<PaymentMethod, Long> {

    Mono<PaymentMethod> findByUserId(Long userId);
}
