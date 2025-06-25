package com.openclassrooms.medilabo.patientService.controller;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public List<PatientDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody @Valid PatientDto patientDto) {
        patientService.createPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PutMapping
//    public PatientEntity updatePatient(@RequestBody PatientEntity patient) {
//        return patientService.updatePatient(patient);
//    }
}
