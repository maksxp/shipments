package com.siaivo.shipments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.siaivo.shipments.repository"})
@SpringBootApplication
public class ShipmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentsApplication.class, args);
	}

}
