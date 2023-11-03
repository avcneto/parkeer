package com.parkeer.parkeer.config;

import com.parkeer.parkeer.entity.park.ParkRedis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${databases.redis.host}")
    private String localhost;

    @Value("${databases.redis.port}")
    private Integer port;

    @Value("${databases.redis.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        var redisStandaloneConfig = new RedisStandaloneConfiguration();
        redisStandaloneConfig.setHostName(localhost);
        redisStandaloneConfig.setPort(port);
        redisStandaloneConfig.setPassword(password);

        return new LettuceConnectionFactory(redisStandaloneConfig);
    }

    @Bean
    public ReactiveRedisOperations<String, ParkRedis> redisOperations(LettuceConnectionFactory connectionFactory) {
        RedisSerializationContext<String, ParkRedis> serializationContext = RedisSerializationContext
                .<String, ParkRedis>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(new GenericToStringSerializer<>(ParkRedis.class))
                .hashKey(new StringRedisSerializer())
                .hashValue(new GenericJackson2JsonRedisSerializer())
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
}
