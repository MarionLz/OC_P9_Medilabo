package com.openclassrooms.medilabo.patientService.controller;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.service.PatientService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger log = LogManager.getLogger(PatientController.class);

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientDto> getAllPatients() {
        log.info("Received request to get all patients");
        List<PatientDto> patients = patientService.getAllPatients();
        log.debug("Returning {} patients", patients.size());
        return patients;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Integer id) {
        log.info("Received request to get patient with ID {}", id);
        PatientDto dto = patientService.getPatientById(id);
        log.debug("Returning patient: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody @Valid PatientDto patientDto) {
        log.info("Received request to create patient: {}", patientDto);
        patientService.createPatient(patientDto);
        log.info("Patient created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @RequestBody @Valid PatientDto patientDto) {
        log.info("Received request to update patient with ID {}: {}", id, patientDto);
        patientService.updatePatient(id, patientDto);
        log.info("Patient with ID {} updated successfully", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
