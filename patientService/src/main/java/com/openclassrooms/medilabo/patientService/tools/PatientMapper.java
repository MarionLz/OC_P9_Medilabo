package com.openclassrooms.medilabo.patientService.tools;

import com.openclassrooms.medilabo.patientService.Dto.PatientDemographicsDto;
import com.openclassrooms.medilabo.patientService.Dto.PatientDto;
import com.openclassrooms.medilabo.patientService.model.PatientEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting between PatientEntity and DTOs.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring")
public interface PatientMapper {

    /**
     * Converts a PatientEntity to a PatientDto.
     *
     * @param entity the PatientEntity to convert
     * @return the corresponding PatientDto
     */
    PatientDto patientToPatientDto(PatientEntity entity);

    /**
     * Converts a PatientDto to a PatientEntity.
     * Inverse mapping of patientToPatientDto.
     *
     * @param dto the PatientDto to convert
     * @return the corresponding PatientEntity
     */
    @InheritInverseConfiguration
    PatientEntity patientDtoToPatient(PatientDto dto);

    /**
     * Updates an existing PatientEntity with values from a PatientDto.
     * The id field is ignored to avoid overwriting the existing id.
     *
     * @param dto the PatientDto containing updated values
     * @param entity the PatientEntity to update
     */
    @Mapping(target = "id", ignore = true)
    void updatePatientFromDto(PatientDto dto, @MappingTarget PatientEntity entity);

    /**
     * Converts a list of PatientEntity objects to a list of PatientDto objects.
     *
     * @param entities the list of PatientEntity objects
     * @return the list of PatientDto objects
     */
    List<PatientDto> patientListToDtoList(List<PatientEntity> entities);

    /**
     * Converts a PatientEntity to a PatientDemographicsDto.
     *
     * @param entity the PatientEntity to convert
     * @return the corresponding PatientDemographicsDto
     */
    PatientDemographicsDto patientToPatientDemographicsDto(PatientEntity entity);
}
