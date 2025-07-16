package com.openclassrooms.medilabo.diabetesReportService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object representing patient demographics.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDemographicsDto {

    private LocalDate dateOfBirth;
    private String gender;

}
