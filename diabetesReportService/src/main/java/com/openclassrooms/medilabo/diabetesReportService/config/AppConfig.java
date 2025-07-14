package com.openclassrooms.medilabo.diabetesReportService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Value("${gateway.username}")  // ajoute ces propriétés dans ton application.properties ou yaml
    private String username;

    @Value("${gateway.password}")
    private String password;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(gatewayUrl) // http://localhost:8080 par exemple
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}

