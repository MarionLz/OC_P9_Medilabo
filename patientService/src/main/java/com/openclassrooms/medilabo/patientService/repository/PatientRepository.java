package com.openclassrooms.medilabo.patientService.repository;

import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing PatientEntity persistence.
 * Extends JpaRepository to provide CRUD operations for PatientEntity.
 */
public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {
}