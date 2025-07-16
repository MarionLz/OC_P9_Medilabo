package com.openclassrooms.medilabo.diabetesReportService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.medilabo.diabetesReportService.dto.PatientDemographicsDto;
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

/**
 * Unit tests for the DiabetesReportService class.
 */
public class DiabetesReportServiceTest {

    /**
     * MockWebServer to simulate external service responses.
     */
    private static MockWebServer mockWebServer;

    /**
     * Instance of DiabetesReportService to be tested.
     */
    private DiabetesReportService diabetesReportService;

    /**
     * ObjectMapper for JSON serialization and deserialization.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Starts the MockWebServer before all tests.
     *
     * @throws IOException if an error occurs during server startup
     */
    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    /**
     * Shuts down the MockWebServer after all tests.
     *
     * @throws IOException if an error occurs during server shutdown
     */
    @AfterAll
    static void shutdownServer() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * Initializes the test environment before each test.
     */
    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());

        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        diabetesReportService = new DiabetesReportService(webClient);
    }

    /**
     * Tests that a male under 30 with 4 triggers returns IN_DANGER risk level.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnInDangerForMaleUnder30With4Triggers() throws Exception {
        PatientDemographicsDto patient = new PatientDemographicsDto(LocalDate.now().minusYears(25), "M");
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

        String risk = diabetesReportService.generateReport(1);

        assertThat(risk).isEqualTo(RiskLevel.IN_DANGER.name());
    }

    /**
     * Tests that a female under 30 with 2 triggers returns BORDERLINE risk level.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnBorderlineForFemaleUnder30With2Triggers() throws Exception {
        PatientDemographicsDto patient = new PatientDemographicsDto(LocalDate.now().minusYears(18), "F");
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

        String risk = diabetesReportService.generateReport(1);

        assertThat(risk).isEqualTo(RiskLevel.BORDERLINE.name());
    }

    /**
     * Tests that a female over 30 with 8 triggers returns EARLY_ONSET risk level.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnEarlyOnsetForFemaleOver30With8Triggers() throws Exception {
        PatientDemographicsDto patient = new PatientDemographicsDto(LocalDate.now().minusYears(46), "F");
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

        String risk = diabetesReportService.generateReport(1);

        assertThat(risk).isEqualTo(RiskLevel.EARLY_ONSET.name());
    }

    /**
     * Tests that no triggers found returns NONE risk level.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnNoneWhenNoTriggersFound() throws Exception {
        PatientDemographicsDto patient = new PatientDemographicsDto(LocalDate.now().minusYears(40), "F");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        List<String> notes = List.of("No relevant info", "Patient seems well");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));

        String risk = diabetesReportService.generateReport(2);

        assertThat(risk).isEqualTo(RiskLevel.NONE.name());
    }

    /**
     * Tests that no notes found returns NONE risk level.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnNoneWhenNoNotesFound() throws Exception {
        PatientDemographicsDto patient = new PatientDemographicsDto(LocalDate.now().minusYears(30), "M");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setBody("[]")
                .addHeader("Content-Type", "application/json"));

        String risk = diabetesReportService.generateReport(3);

        assertThat(risk).isEqualTo(RiskLevel.NONE.name());
    }
}