package com.parkeer.parkeer.config;

import com.parkeer.parkeer.handler.park.ParkHandler;
import com.parkeer.parkeer.handler.payment_method.PaymentMethodHandler;
import com.parkeer.parkeer.handler.receipty.ReceiptHandler;
import com.parkeer.parkeer.handler.user.UserHandler;
import com.parkeer.parkeer.handler.vehicle.VehicleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    private static final String PING_PATH = "/ping";
    private static final String PARKEER_PATH = "/parkeer";
    private static final String USER_SEARCH_PATH = "/user/search";
    private static final String USER_PATH = "/user";
    private static final String VEHICLE_PATH = "/vehicle";
    private static final String VEHICLE_SEARCH_PATH = "/vehicle/search";
    private static final String PARK_PATH = "/park";
    private static final String PARK_SEARCH_PATH = "/park/search";
    private static final String UNPARK = "/unpark";
    private static final String PAYMENT_METHOD = "/payment/method";
    private static final String RECEIPT = "/receipt";

    @Bean
    public RouterFunction<ServerResponse> routes(
            final UserHandler userHandler,
            final VehicleHandler vehicleHandler,
            final ParkHandler parkHandler,
            final PaymentMethodHandler paymentMethodHandler,
            final ReceiptHandler receiptHandler
    ) {
        return nest(path(PARKEER_PATH).and(accept(APPLICATION_JSON)),
                route(GET((PING_PATH)), userHandler::ping)
                        .andRoute(POST(USER_PATH), userHandler::createUser)
                        .andRoute(PATCH(USER_PATH), userHandler::updateUserById)
                        .andRoute(DELETE(USER_PATH), userHandler::deleteById)
                        .andRoute(GET(USER_SEARCH_PATH), userHandler::getUserByCpfOrEmailOrId)
                        .andRoute(POST(VEHICLE_PATH), vehicleHandler::addVehicle)
                        .andRoute(GET(VEHICLE_SEARCH_PATH), vehicleHandler::getVehicleByIdOrUserIdOrPlate)
                        .andRoute(DELETE(VEHICLE_PATH), vehicleHandler::deleteByVehicleId)
                        .andRoute(PATCH(VEHICLE_PATH), vehicleHandler::updateVehicleById)
                        .andRoute(POST(PARK_PATH), parkHandler::park)
                        .andRoute(POST(UNPARK), parkHandler::unPark)
                        .andRoute(POST(PAYMENT_METHOD), paymentMethodHandler::addPaymentMethod)
                        .andRoute(DELETE(PAYMENT_METHOD), paymentMethodHandler::deletePaymentMethodById)
                        .andRoute(PATCH(PAYMENT_METHOD), paymentMethodHandler::updateByPaymentMethodId)
                        .andRoute(GET(PAYMENT_METHOD), paymentMethodHandler::getAllPaymentMethodOrById)
                        .andRoute(GET(RECEIPT), receiptHandler::getReceiptByUserIdOrPlate)
                        .andRoute(GET(PARK_SEARCH_PATH), parkHandler::getParkByPlate)
        );
    }
}
