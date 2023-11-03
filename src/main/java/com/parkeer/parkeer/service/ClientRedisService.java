package com.parkeer.parkeer.service;

//@Slf4j
//@Service
//@EnableScheduling
public class ClientRedisService {

//    private static final String CLIENT_IS_EMPTY = "client list is empty";
//    private static final String EMAIL_OR_CPF_REGISTERED = "email or CPF already registered";
//    private static final long MINUTES = (1000 * 60) * 1; // sincronização a cada 1 minuto
//    private final ClientRepositoryImpl redisRepository;
//
//    public ClientRedisService(ClientRepositoryImpl redisRepository) {
//        this.redisRepository = redisRepository;
//    }
//
//    private Mono<ClientRedis> save(ClientRedis clientRedis) {
//        return this.redisRepository.save(clientRedis);
//    }
//
//    private Flux<ClientRedis> findAll() {
//        return redisRepository.findAll();
//    }

//    @Scheduled(fixedDelay = MINUTES)
//    public void synchronizeDatabase() {
//        findAll()
//                .map(User::new)
//                .switchIfEmpty(Mono.fromRunnable(() -> log.info(CLIENT_IS_EMPTY)))
//                .collectList()
//                .flatMapMany(userRepository::saveAll)
//                .map(Client::getId)
//                .collectList()
//                .flatMapMany(it -> redisRepository.deleteAll(Flux.fromIterable(it)))
//                .subscribe();
//    }

}
