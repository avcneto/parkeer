package com.parkeer.parkeer.repository.receipt;

import com.parkeer.parkeer.entity.receipt.Receipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReceiptRepository extends R2dbcRepository<Receipt, Long> {

    Flux<Receipt> findReceiptByUserIdOrPlate(Long userId, String plate);
}
