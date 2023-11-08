package com.parkeer.parkeer.handler.payment_method;

import com.parkeer.parkeer.dto.payment_method.PaymentMethodDTO;
import com.parkeer.parkeer.dto.payment_method.PaymentUpdateMethodDTO;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.service.payment_method.PaymentMethodService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;

@Component
public record PaymentMethodHandler(
        PaymentMethodService paymentMethodService
) {

    private static final String BODY_IS_EMPTY = "body is empty";
    private static final String PAYMENT_METHOD_ID = "/payment/method/%s";

    public Mono<ServerResponse> addPaymentMethod(final ServerRequest request) {
        return request
                .bodyToMono(PaymentMethodDTO.class)
                .switchIfEmpty(Mono.error(new BadRequestException(BODY_IS_EMPTY)))
                .flatMap(paymentMethodService::addPaymentMethod)
                .flatMap(paymentMethod -> ServerResponse
                        .created(URI.create(format(PAYMENT_METHOD_ID, paymentMethod.getId())))
                        .bodyValue(paymentMethod))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> deletePaymentMethodById(final ServerRequest request) {
        return paymentMethodService.deletePaymentMethodById(request.queryParams())
                .then(Mono.defer(() -> noContent().build()));
    }

    public Mono<ServerResponse> updateByPaymentMethodId(final ServerRequest request) {
        return request
                .bodyToMono(PaymentUpdateMethodDTO.class)
                .switchIfEmpty(Mono.error(new BadRequestException(BODY_IS_EMPTY)))
                .flatMap(userUpdateDTO -> paymentMethodService.updateVehicleById(request.queryParams(), userUpdateDTO))
                .flatMap(paymentUpdateMethodDTO -> ServerResponse.ok().bodyValue(paymentUpdateMethodDTO))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAllPaymentMethodOrById(final ServerRequest request) {
        return paymentMethodService.getAllPaymentMethodOrById(request.queryParams())
                .collectList()
                .flatMap(paymentMethods -> ServerResponse.ok().bodyValue(paymentMethods))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
