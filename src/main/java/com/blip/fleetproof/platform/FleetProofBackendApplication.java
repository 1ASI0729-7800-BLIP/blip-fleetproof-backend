package com.blip.fleetproof.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FleetProofBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetProofBackendApplication.class, args);
    }
}
