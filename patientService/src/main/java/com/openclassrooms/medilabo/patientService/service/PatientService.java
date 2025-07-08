package com.openclassrooms.medilabo.patientService.service;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.exceptions.PatientNotFoundException;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import com.openclassrooms.medilabo.patientService.repository.PatientRepository;
import com.openclassrooms.medilabo.patientService.tools.PatientMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PatientService {

    private static final Logger log = LogManager.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientDto> getAllPatients() {
        log.info("Fetching all patients from repository");
        List<PatientEntity> entities = patientRepository.findAll();
        log.debug("Found {} patient records", entities.size());
        return patientMapper.patientListToDtoList(entities);
    }

    public PatientDto getPatientById(Integer id) {
        log.info("Fetching patient with ID {}", id);
        PatientEntity patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        return patientMapper.patientToPatientDto(patient);
    }

    public void createPatient(PatientDto dto) {
        log.info("Creating new patient: {}", dto);
        PatientEntity entity = patientMapper.patientDtoToPatient(dto);
        patientRepository.save(entity);
        log.info("Patient saved successfully with ID {}", entity.getId());
    }

    public void updatePatient(Integer id, PatientDto dto) {
        log.info("Updating patient with ID {}", id);
        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        patientMapper.updatePatientFromDto(dto, entity);
        patientRepository.save(entity);
        log.info("Patient with ID {} updated successfully", id);
    }
}
