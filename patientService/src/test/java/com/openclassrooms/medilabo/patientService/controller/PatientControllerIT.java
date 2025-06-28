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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    private Integer aliceId;

    @BeforeEach
    void setup() {
        patientRepository.deleteAll();

        PatientEntity alice = patientRepository.save(new PatientEntity("Alice", "Dupont", LocalDate.of(1985, 5, 12),
                "F", "111-222-3333","12 rue A"));
        aliceId = alice.getId();
        patientRepository.save(new PatientEntity("Bob", "Martin", LocalDate.of(1990, 10, 20),
                "M", "444-555-6666", "34 avenue B"));
    }

    @Test
    void shouldReturnAllPatients() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    @Test
    void shouldCreateNewPatient() throws Exception {
        PatientDto patientDto = new PatientDto();
        LocalDate dateOfBirth = LocalDate.of(1980, 1, 1);
        patientDto.setFirstName("John");
        patientDto.setLastName("Doe");
        patientDto.setGender("M");
        patientDto.setDateOfBirth(dateOfBirth);
        patientDto.setAddress("123 rue de Paris");
        patientDto.setPhoneNumber("123-456-7890");

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidPatient() throws Exception {
        PatientDto invalidPatient = new PatientDto();
        LocalDate dateOfBirth = LocalDate.of(1980, 1, 1);
        invalidPatient.setFirstName("Marie");
        invalidPatient.setLastName("Martin");
        invalidPatient.setGender("F");
        invalidPatient.setDateOfBirth(dateOfBirth);
        invalidPatient.setAddress("26 rue des Lilas");
        invalidPatient.setPhoneNumber("0603246743");

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        PatientDto updatedPatient = new PatientDto();
        LocalDate dateOfBirth = LocalDate.of(1990, 2, 2);
        updatedPatient.setFirstName("Alice");
        updatedPatient.setLastName("Dupont");
        updatedPatient.setGender("F");
        updatedPatient.setDateOfBirth(dateOfBirth);
        updatedPatient.setAddress("456 rue de Lyon");
        updatedPatient.setPhoneNumber("777-888-9999");

        mockMvc.perform(put("/patients/" + aliceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPatient() throws Exception {
        PatientDto patientDto = new PatientDto();
        patientDto.setFirstName("Ghost");
        patientDto.setLastName("Patient");
        patientDto.setGender("M");
        patientDto.setDateOfBirth(LocalDate.of(1970, 1, 1));
        patientDto.setAddress("Nulle part");
        patientDto.setPhoneNumber("000-000-0000");

        mockMvc.perform(put("/patients/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Patient not found with id: 9999"));
    }
}

