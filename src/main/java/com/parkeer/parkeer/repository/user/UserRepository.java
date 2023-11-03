package com.parkeer.parkeer.repository.user;

import com.parkeer.parkeer.entity.user.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<Boolean> existsByEmailOrCpf(String email, String cpf);
    Mono<User> findByCpfOrEmailOrId(String cpf, String email, Long id);
}
