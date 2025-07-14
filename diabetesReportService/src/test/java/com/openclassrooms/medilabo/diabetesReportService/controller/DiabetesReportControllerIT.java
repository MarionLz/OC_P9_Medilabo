package com.openclassrooms.medilabo.diabetesReportService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.medilabo.diabetesReportService.dto.PatientDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
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

@SpringBootTest
@ContextConfiguration(initializers = DiabetesReportControllerIT.MockWebServerInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiabetesReportControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testGetDiabetesReportWithMockedServices() throws Exception {
        // Mock patient demographic response
        PatientDto patient = new PatientDto(LocalDate.now().minusYears(25), "M");
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
