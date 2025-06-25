package com.openclassrooms.medilabo.patientService.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientDto {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is mandatory")
    @Pattern(regexp = "F|M", message = "Gender must be either 'F' or 'M'")
    private String gender;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Phone number must be in the format xxx-xxx-xxxx")
    private String phoneNumber;

    private String address;
}
