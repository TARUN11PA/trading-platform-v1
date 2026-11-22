package com.stocktrading.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockTradingPlatformApplication {

	public static void main(String[] args) {

		SpringApplication.run(StockTradingPlatformApplication.class, args);
	}

}
