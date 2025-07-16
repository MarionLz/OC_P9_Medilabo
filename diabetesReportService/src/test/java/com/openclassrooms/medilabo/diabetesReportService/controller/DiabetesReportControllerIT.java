package com.openclassrooms.medilabo.diabetesReportService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.medilabo.diabetesReportService.dto.PatientDemographicsDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DiabetesReportController.
 */
@SpringBootTest
@ContextConfiguration(initializers = DiabetesReportControllerIT.MockWebServerInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiabetesReportControllerIT {

    /**
     * The web application context used to build MockMvc.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * MockMvc instance for performing HTTP requests in tests.
     */
    private MockMvc mockMvc;

    /**
     * MockWebServer to simulate external service responses.
     */
    private static MockWebServer mockWebServer;

    /**
     * ObjectMapper for serializing and deserializing JSON, including Java time types.
     */
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Initializes MockMvc before each test.
     */
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * Shuts down the MockWebServer after all tests have run.
     *
     * @throws IOException if an error occurs during shutdown
     */
    @AfterAll
    static void shutdownServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    /**
     * Tests the diabetes report endpoint with mocked patient and notes services.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetDiabetesReportWithMockedServices() throws Exception {
        // Mock patient demographic response
        PatientDemographicsDto patient = new PatientDemographicsDto(LocalDate.now().minusYears(25), "M");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        // Mock notes response
        List<String> notes = List.of("Poids élevé", "Fumeur chronique", "Hémoglobine A1C élevée");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(get("/diabetes-report/patient/1")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("IN_DANGER")));
    }

    /**
     * ApplicationContextInitializer to start MockWebServer and set gateway URL property.
     */
    static class MockWebServerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            try {
                mockWebServer = new MockWebServer();
                mockWebServer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TestPropertyValues.of(
                    "gateway.url=" + mockWebServer.url("/").toString()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}