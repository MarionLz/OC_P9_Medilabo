package com.openclassrooms.medilabo.notesService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a request to add a note for a patient.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    private String patient;
    private String note;
}
