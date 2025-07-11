package com.openclassrooms.medilabo.notesService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    private String patient;
    private String note;
}
