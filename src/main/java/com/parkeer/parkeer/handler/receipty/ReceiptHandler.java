package com.parkeer.parkeer.handler.receipty;

import com.parkeer.parkeer.service.receipt.ReceiptService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record ReceiptHandler(
        ReceiptService receiptService
) {
    public Mono<ServerResponse> getReceiptByUserIdOrPlate(final ServerRequest request) {
        return receiptService.getReceiptByUserIdOrPlate(request.queryParams())
                .collectList()
                .flatMap(vehicle -> ServerResponse.ok().bodyValue(vehicle))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
