package com.openclassrooms.medilabo.diabetesReportService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(gatewayUrl) // http://localhost:8080 par exemple
                .build();
    }
}

