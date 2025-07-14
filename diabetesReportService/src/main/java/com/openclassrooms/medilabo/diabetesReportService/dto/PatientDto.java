package com.openclassrooms.medilabo.diabetesReportService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private LocalDate dateOfBirth;
    private String gender;

}
