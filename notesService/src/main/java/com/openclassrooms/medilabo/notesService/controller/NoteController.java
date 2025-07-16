package com.openclassrooms.medilabo.notesService.controller;

import com.openclassrooms.medilabo.notesService.Dto.NoteRequest;
import com.openclassrooms.medilabo.notesService.service.NoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * REST controller for managing patient notes.
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    private static final Logger log = LogManager.getLogger(NoteController.class);

    private final NoteService noteService;

    /**
     * Constructor for NoteController.
     *
     * @param noteService the service handling note operations
     */
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Retrieves all notes for a given patient by their ID.
     *
     * @param patientId the ID of the patient
     * @return ResponseEntity containing the list of notes
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getNotesByPatientId(@PathVariable Integer patientId) {
        log.info("Received request to get all notes for patient with ID {}", patientId);
        List<String> notes = noteService.getNotesByPatientId(patientId)
                .orElse(Collections.emptyList());

        log.debug("Returning {} notes for patient ID {}", notes.size(), patientId);
        return ResponseEntity.ok(notes);
    }

    /**
     * Adds a new note to a patient.
     *
     * @param patientId the ID of the patient
     * @param noteRequest the note request containing note details
     * @return ResponseEntity indicating the result of the operation
     */
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<?> addNoteToPatient(@PathVariable Integer patientId, @RequestBody NoteRequest noteRequest) {
        log.info("Received request to add note to patient with ID {}", patientId);
        boolean added = noteService.addNoteToPatient(patientId, noteRequest.getNote(), noteRequest.getPatient());
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Note ajoutée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de la note.");
        }
    }

}