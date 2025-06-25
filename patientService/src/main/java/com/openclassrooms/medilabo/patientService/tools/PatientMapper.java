package com.openclassrooms.medilabo.patientService.tools;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    // Entity → DTO
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    PatientDto patientToPatientDto(PatientEntity entity);

    // DTO → Entity (inverse automatique du mapping précédent)
    @InheritInverseConfiguration
    PatientEntity patientDtoToPatient(PatientDto dto);

    // Liste d'entités → Liste de DTOs
    List<PatientDto> patientListToDtoList(List<PatientEntity> entities);

    // Liste de DTOs → Liste d'entités
    List<PatientEntity> dtoListToPatientList(List<PatientDto> dtos);
}
