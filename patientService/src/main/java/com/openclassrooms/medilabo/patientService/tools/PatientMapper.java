package com.openclassrooms.medilabo.patientService.tools;

import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    // Entity → DTO
    PatientDto patientToPatientDto(PatientEntity entity);

    // DTO → Entity (inverse automatique du mapping précédent)
    @InheritInverseConfiguration
    PatientEntity patientDtoToPatient(PatientDto dto);

    // Mise à jour d'une entité à partir d'un DTO
    @Mapping(target = "id", ignore = true) // on évite d’écraser l’id existant
    void updatePatientFromDto(PatientDto dto, @MappingTarget PatientEntity entity);

    // Liste d'entités → Liste de DTOs
    List<PatientDto> patientListToDtoList(List<PatientEntity> entities);

    // Liste de DTOs → Liste d'entités
    List<PatientEntity> dtoListToPatientList(List<PatientDto> dtos);
}
