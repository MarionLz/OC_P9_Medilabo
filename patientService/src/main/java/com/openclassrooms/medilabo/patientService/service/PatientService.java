package com.openclassrooms.medilabo.patientService.service;

import com.openclassrooms.medilabo.patientService.Dto.PatientDemographicsDto;
import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.exceptions.PatientNotFoundException;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import com.openclassrooms.medilabo.patientService.repository.PatientRepository;
import com.openclassrooms.medilabo.patientService.tools.PatientMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing patient operations.
 */
@Service
public class PatientService {

    private static final Logger log = LogManager.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    /**
     * Constructs a PatientService with the given repository and mapper.
     *
     * @param patientRepository the repository for patient entities
     * @param patientMapper the mapper for converting between entities and DTOs
     */
    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    /**
     * Retrieves all patients from the repository.
     *
     * @return a list of PatientDto objects representing all patients
     */
    public List<PatientDto> getAllPatients() {
        log.info("Fetching all patients from repository");
        List<PatientEntity> entities = patientRepository.findAll();
        log.debug("Found {} patient records", entities.size());
        return patientMapper.patientListToDtoList(entities);
    }

    /**
     * Retrieves a patient by their unique ID.
     *
     * @param id the ID of the patient
     * @return the PatientDto representing the patient
     * @throws PatientNotFoundException if the patient is not found
     */
    public PatientDto getPatientById(Integer id) {
        log.info("Fetching patient with ID {}", id);
        PatientEntity patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        return patientMapper.patientToPatientDto(patient);
    }

    /**
     * Creates a new patient in the repository.
     *
     * @param dto the PatientDto containing patient information
     */
    public void createPatient(PatientDto dto) {
        log.info("Creating new patient: {}", dto);
        PatientEntity entity = patientMapper.patientDtoToPatient(dto);
        patientRepository.save(entity);
        log.info("Patient saved successfully with ID {}", entity.getId());
    }

    /**
     * Updates an existing patient with the given ID.
     *
     * @param id the ID of the patient to update
     * @param dto the PatientDto containing updated information
     * @throws PatientNotFoundException if the patient is not found
     */
    public void updatePatient(Integer id, PatientDto dto) {
        log.info("Updating patient with ID {}", id);
        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        patientMapper.updatePatientFromDto(dto, entity);
        patientRepository.save(entity);
        log.info("Patient with ID {} updated successfully", id);
    }

    /**
     * Retrieves demographic information for a patient by their ID.
     *
     * @param id the ID of the patient
     * @return the PatientDemographicsDto containing demographic information
     * @throws PatientNotFoundException if the patient is not found
     */
    public PatientDemographicsDto getDemographicsInfoByPatientId(Integer id) {
        log.info("Fetching demographics info for patient with ID {}", id);
        PatientEntity patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        return patientMapper.patientToPatientDemographicsDto(patient);
    }
}