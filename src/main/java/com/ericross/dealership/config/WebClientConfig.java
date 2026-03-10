package com.ericross.dealership.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient nhtsaWebClient() {
        return WebClient.builder()
                .baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles")
                .build();
    }
}

