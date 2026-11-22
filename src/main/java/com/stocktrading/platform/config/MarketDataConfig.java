
package com.stocktrading.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MarketDataConfig {

    @Value("${indianapi.base-url}")
    private String baseUrl;

    @Value("${indianapi.api-key}")
    private String apiKey;

    @Bean
    public RestClient marketDataRestClient() {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("accept", "application/json")
                .build();
    }
}
