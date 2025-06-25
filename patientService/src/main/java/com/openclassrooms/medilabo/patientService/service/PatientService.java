package com.openclassrooms.medilabo.patientService.service;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import com.openclassrooms.medilabo.patientService.repository.PatientRepository;
import com.openclassrooms.medilabo.patientService.tools.PatientMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PatientService {

    private PatientRepository patientRepository;
    private PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientDto> getAllPatients() {
        List<PatientEntity> entities = patientRepository.findAll();
        return patientMapper.patientListToDtoList(entities);
    }

    public void createPatient(PatientDto dto) {
        PatientEntity entity = patientMapper.patientDtoToPatient(dto);
        patientRepository.save(entity);
    }

    public void updatePatient(Integer id, PatientDto dto) {
        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Patient not found with id: " + id));
        patientMapper.updatePatientFromDto(dto, entity);
        patientRepository.save(entity);
    }
}
