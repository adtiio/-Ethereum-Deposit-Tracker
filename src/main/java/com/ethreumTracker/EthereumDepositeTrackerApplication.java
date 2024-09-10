package com.ethreumTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EthereumDepositeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EthereumDepositeTrackerApplication.class, args);
	}

}
