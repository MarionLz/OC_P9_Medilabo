package com.openclassrooms.medilabo.patientService.repository;

import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {
}
