package com.openclassrooms.medilabo.diabetesReportService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.medilabo.diabetesReportService.dto.PatientDto;
import com.openclassrooms.medilabo.diabetesReportService.enums.RiskLevel;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DiabetesReportServiceTest {

    private static MockWebServer mockWebServer;
    private DiabetesReportService diabetesReportService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());

        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        diabetesReportService = new DiabetesReportService(webClient);
    }

    @Test
    void shouldReturnInDangerForMaleUnder30With4Triggers() throws Exception {
        // Arrange
        PatientDto patient = new PatientDto(LocalDate.now().minusYears(25), "M");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        List<String> notes = List.of(
                "L'hémoglobine A1C est élevée",
                "Perte de poids et tabagisme",
                "Microalbumine anormale"
        );
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));

        // Act
        String risk = diabetesReportService.generateReport(1);

        // Assert
        assertThat(risk).isEqualTo(RiskLevel.IN_DANGER.name());
    }

    @Test
    void shouldReturnBorderlineForFemaleUnder30With2Triggers() throws Exception {
        // Arrange
        PatientDto patient = new PatientDto(LocalDate.now().minusYears(18), "F");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        List<String> notes = List.of(
                "L'hémoglobine A1C est élevée",
                "Perte de poids"
        );
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));

        // Act
        String risk = diabetesReportService.generateReport(1);

        // Assert
        assertThat(risk).isEqualTo(RiskLevel.BORDERLINE.name());
    }

    @Test
    void shouldReturnEarlyOnsetForFemaleOver30With8Triggers() throws Exception {
        // Arrange
        PatientDto patient = new PatientDto(LocalDate.now().minusYears(46), "F");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        List<String> notes = List.of(
                "L'hémoglobine A1C est élevée",
                "Perte de poids",
                "Fumeuse pendant 10 ans, arrêt l'année dernière mais rechute possible",
                "Cholestérol et microalbumine anormale",
                "Vertiges fréquents"
        );
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));

        // Act
        String risk = diabetesReportService.generateReport(1);

        // Assert
        assertThat(risk).isEqualTo(RiskLevel.EARLY_ONSET.name());
    }

    @Test
    void shouldReturnNoneWhenNoTriggersFound() throws Exception {
        // Arrange
        PatientDto patient = new PatientDto(LocalDate.now().minusYears(40), "F");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        List<String> notes = List.of("No relevant info", "Patient seems well");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));

        // Act
        String risk = diabetesReportService.generateReport(2);

        // Assert
        assertThat(risk).isEqualTo(RiskLevel.NONE.name());
    }

    @Test
    void shouldReturnNoneWhenNoNotesFound() throws Exception {
        // Arrange
        PatientDto patient = new PatientDto(LocalDate.now().minusYears(30), "M");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setBody("[]")
                .addHeader("Content-Type", "application/json"));

        // Act
        String risk = diabetesReportService.generateReport(3);

        // Assert
        assertThat(risk).isEqualTo(RiskLevel.NONE.name());
    }
}

