package com.openclassrooms.medilabo.patientService.exceptions;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Integer id) {
        super("Patient not found with id: " + id);
    }
}
