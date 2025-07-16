package com.openclassrooms.medilabo.patientService.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exception thrown when a patient with the specified ID is not found.
 */
public class PatientNotFoundException extends RuntimeException {

    private static final Logger log = LogManager.getLogger(PatientNotFoundException.class);

    /**
     * Constructs a new PatientNotFoundException with a detailed message.
     *
     * @param id the ID of the patient that was not found
     */
    public PatientNotFoundException(Integer id) {
        super("Patient not found with id: " + id);
        log.error("Patient with ID {} not found for update", id);
    }
}