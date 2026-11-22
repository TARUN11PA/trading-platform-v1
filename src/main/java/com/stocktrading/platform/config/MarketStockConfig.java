
package com.stocktrading.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MarketStockConfig {

    @Bean
    public Map<String, String> marketStocks() {

        return Map.of(
                "Tata Consultancy Services", "TCS",
                "Infosys", "INFY"
        );
    }
}
