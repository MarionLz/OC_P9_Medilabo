package com.openclassrooms.medilabo.patientService.controller;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Integer id) {
        PatientDto dto = patientService.getPatientById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody @Valid PatientDto patientDto) {
        patientService.createPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @RequestBody @Valid PatientDto patientDto) {
        patientService.updatePatient(id, patientDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
