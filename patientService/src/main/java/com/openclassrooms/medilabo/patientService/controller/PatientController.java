package com.openclassrooms.medilabo.patientService.controller;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.Dto.PatientDemographicsDto;
import com.openclassrooms.medilabo.patientService.service.PatientService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing patient data.
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger log = LogManager.getLogger(PatientController.class);

    private final PatientService patientService;

    /**
     * Constructor for PatientController.
     *
     * @param patientService the service handling patient operations
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Retrieves all patients.
     *
     * @return a list of PatientDto objects
     */
    @GetMapping
    public List<PatientDto> getAllPatients() {
        log.info("Received request to get all patients");
        List<PatientDto> patients = patientService.getAllPatients();
        log.debug("Returning {} patients", patients.size());
        return patients;
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id the ID of the patient
     * @return a ResponseEntity containing the PatientDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Integer id) {
        log.info("Received request to get patient with ID {}", id);
        PatientDto dto = patientService.getPatientById(id);
        log.debug("Returning patient: {}", dto);
        return ResponseEntity.ok(dto);
    }

    /**
     * Creates a new patient.
     *
     * @param patientDto the patient data to create
     * @return a ResponseEntity with status CREATED
     */
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody @Valid PatientDto patientDto) {
        log.info("Received request to create patient: {}", patientDto);
        patientService.createPatient(patientDto);
        log.info("Patient created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates an existing patient.
     *
     * @param id the ID of the patient to update
     * @param patientDto the updated patient data
     * @return a ResponseEntity with status OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @RequestBody @Valid PatientDto patientDto) {
        log.info("Received request to update patient with ID {}: {}", id, patientDto);
        patientService.updatePatient(id, patientDto);
        log.info("Patient with ID {} updated successfully", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Retrieves demographic information for a patient by their ID.
     *
     * @param id the ID of the patient
     * @return a ResponseEntity containing the PatientDemographicsDto
     */
    @GetMapping("/{id}/demographics")
    public ResponseEntity<PatientDemographicsDto> getDemographicsInfoByPatientId(@PathVariable Integer id) {
        log.info("Received request to get demographics info with ID {}", id);
        PatientDemographicsDto dto = patientService.getDemographicsInfoByPatientId(id);
        log.debug("Returning patient demographics info: {}", dto);
        return ResponseEntity.ok(dto);
    }
}