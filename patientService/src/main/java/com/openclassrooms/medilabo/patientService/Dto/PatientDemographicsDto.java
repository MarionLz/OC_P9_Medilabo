package com.openclassrooms.medilabo.patientService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object representing patient demographic information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDemographicsDto {

    private LocalDate dateOfBirth;
    private String gender;
}
