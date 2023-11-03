package com.parkeer.parkeer.handler.user;

import com.parkeer.parkeer.dto.user.UserDTO;
import com.parkeer.parkeer.dto.user.UserUpdateDTO;
import com.parkeer.parkeer.exception.BadRequestException;
import com.parkeer.parkeer.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public record UserHandler(
        UserService userService
) {
    private static final String PONG = "pong";
    private static final String BODY_IS_EMPTY = "body is empty";
    private static final String USER_ID = "/user/%s";

    public Mono<ServerResponse> ping(final ServerRequest request) {
        return ok().bodyValue(PONG);
    }

    public Mono<ServerResponse> createUser(final ServerRequest request) {
        return request
                .bodyToMono(UserDTO.class)
                .switchIfEmpty(Mono.error(new BadRequestException(BODY_IS_EMPTY)))
                .flatMap(userService::createUser)
                .flatMap(user -> ServerResponse
                        .created(URI.create(format(USER_ID, user.getId())))
                        .bodyValue(user));
    }

    public Mono<ServerResponse> getUserByCpfOrEmailOrId(final ServerRequest request) {
        return userService.getUserByCpfOrEmailOrId(request.queryParams())
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> updateUserById(final ServerRequest request) {
        return request
                .bodyToMono(UserUpdateDTO.class)
                .switchIfEmpty(Mono.error(new BadRequestException(BODY_IS_EMPTY)))
                .flatMap(userUpdateDTO -> userService.updateUserById(request.queryParams(), userUpdateDTO))
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteById(final ServerRequest request) {
        return userService.deleteUserById(request.queryParams())
                .then(Mono.defer(() -> noContent().build()));
    }
}
