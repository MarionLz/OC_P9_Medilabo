package com.openclassrooms.medilabo.patientService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import com.openclassrooms.medilabo.patientService.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PatientController.
 * Tests REST endpoints for patient management.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PatientControllerIT {

    /** MockMvc for performing HTTP requests in tests. */
    @Autowired
    private MockMvc mockMvc;

    /** ObjectMapper for serializing/deserializing JSON. */
    @Autowired
    private ObjectMapper objectMapper;

    /** Repository for managing patient entities in tests. */
    @Autowired
    private PatientRepository patientRepository;

    /** ID of the patient Alice, used in tests. */
    private Integer aliceId;

    /**
     * Sets up the test data before each test.
     * Clears the repository and adds two patients.
     */
    @BeforeEach
    void setup() {
        patientRepository.deleteAll();

        PatientEntity alice = patientRepository.save(new PatientEntity("Alice", "Dupont", LocalDate.of(1985, 5, 12),
                "F", "111-222-3333","12 rue A"));
        aliceId = alice.getId();
        patientRepository.save(new PatientEntity("Bob", "Martin", LocalDate.of(1990, 10, 20),
                "M", "444-555-6666", "34 avenue B"));
    }

    /**
     * Tests that all patients are returned by the GET /patients endpoint.
     */
    @Test
    void shouldReturnAllPatients() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    /**
     * Tests that a patient is returned by their ID using GET /patients/{id}.
     */
    @Test
    void shouldReturnPatientWithGivenId() throws Exception {
        mockMvc.perform(get("/patients/"+ aliceId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    /**
     * Tests that a 404 error is returned when requesting a non-existent patient.
     */
    @Test
    void shouldReturnNotFoundExceptionWithUnknownId() throws Exception {
        mockMvc.perform(get("/patients/"+ 9999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Patient not found with id: 9999"));
    }

    /**
     * Tests that a new patient can be created using POST /patients.
     */
    @Test
    void shouldCreateNewPatient() throws Exception {
        PatientDto patientDto = new PatientDto("John", "Doe", LocalDate.of(1980, 1, 1),
                "M", "123-456-7890", "123 rue de Paris");

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated());
    }

    /**
     * Tests that a bad request is returned when creating an invalid patient.
     */
    @Test
    void shouldReturnBadRequestWhenCreatingInvalidPatient() throws Exception {
        PatientDto invalidPatient = new PatientDto("Marie", "Martin", LocalDate.of(1980, 1, 1),
                "F", "0603246743", "26 rue des Lilas");

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that an existing patient can be updated using PUT /patients/{id}.
     */
    @Test
    void shouldUpdatePatient() throws Exception {
        PatientDto updatedPatient = new PatientDto("Alice", "Dupont", LocalDate.of(1990, 2, 2),
                "F", "777-888-9999", "456 rue de Lyon");

        mockMvc.perform(put("/patients/" + aliceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk());
    }

    /**
     * Tests that a 404 error is returned when updating a non-existent patient.
     */
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPatient() throws Exception {
        PatientDto patientDto = new PatientDto("Ghost", "Patient", LocalDate.of(1970, 1, 1),
                "M", "000-000-0000", "Nulle part");

        mockMvc.perform(put("/patients/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Patient not found with id: 9999"));
    }
}