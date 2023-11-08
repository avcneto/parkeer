package com.parkeer.parkeer.service.receipt;

import com.parkeer.parkeer.dto.receipt.ReceiptDTO;
import com.parkeer.parkeer.repository.receipt.ReceiptRepository;
import com.parkeer.parkeer.util.QueryParams;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Flux<ReceiptDTO> getReceiptByUserIdOrPlate(final MultiValueMap<String, String> params) {
        return Flux
                .just(params)
                .flatMap(it -> {
                    var query = new QueryParams(it);
                    query.validateUserIdAndPlate();

                    return receiptRepository.findReceiptByUserIdOrPlate(query.getUserId(), query.getPlate())
                            .map(ReceiptDTO::new);
                });
    }
}
