package com.openclassrooms.medilabo.patientService.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PatientNotFoundException extends RuntimeException {

    private static final Logger log = LogManager.getLogger(PatientNotFoundException.class);

    public PatientNotFoundException(Integer id) {
        super("Patient not found with id: " + id);
        log.error("Patient with ID {} not found for update", id);
    }
}
