package com.parkeer.parkeer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class ParkeerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkeerApplication.class, args);
    }
}
