package com.openclassrooms.medilabo.patientService.service;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import com.openclassrooms.medilabo.patientService.repository.PatientRepository;
import com.openclassrooms.medilabo.patientService.tools.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    private void validateDateFormat(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date of birth. Format must be : YYYY-MM-DD");
        }
    }

    public List<PatientDto> getAllPatients() {
        List<PatientEntity> entities = patientRepository.findAll();
        return patientMapper.patientListToDtoList(entities);
    }

    public void createPatient(PatientDto dto) {
        validateDateFormat(dto.getDateOfBirth());
        PatientEntity entity = patientMapper.patientDtoToPatient(dto);
        patientRepository.save(entity);
    }
}
