package com.openclassrooms.medilabo.diabetesReportService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration class for customizing Jackson ObjectMapper.
 * Registers the JavaTimeModule to handle Java 8 date and time API serialization and deserialization.
 */
@Configuration
public class JacksonConfig {

    /**
     * The ObjectMapper used for JSON serialization and deserialization.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Registers the JavaTimeModule with the ObjectMapper after bean construction.
     * This enables support for Java 8 date and time types.
     */
    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }
}

