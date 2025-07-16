package com.openclassrooms.medilabo.patientService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Global exception handler for the patient service.
 * Handles custom exceptions and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles PatientNotFoundException and returns a NOT_FOUND status with an error message.
     *
     * @param ex the PatientNotFoundException thrown
     * @return a map containing the error message
     */
    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlePatientNotFound(PatientNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }
}
