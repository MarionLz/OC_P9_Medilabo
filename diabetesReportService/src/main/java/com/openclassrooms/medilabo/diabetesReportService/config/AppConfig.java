package com.openclassrooms.medilabo.diabetesReportService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for application beans.
 * Sets up the WebClient with basic authentication and base URL.
 */
@Configuration
public class AppConfig {

    /**
     * The base URL for the gateway.
     */
    @Value("${gateway.url}")
    private String gatewayUrl;

    /**
     * The username for gateway authentication.
     */
    @Value("${gateway.username}")
    private String username;

    /**
     * The password for gateway authentication.
     */
    @Value("${gateway.password}")
    private String password;

    /**
     * Creates and configures a WebClient bean with basic authentication.
     *
     * @return a configured WebClient instance
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(gatewayUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}
