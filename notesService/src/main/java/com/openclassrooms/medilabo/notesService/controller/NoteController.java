package com.openclassrooms.medilabo.notesService.controller;

import com.openclassrooms.medilabo.notesService.service.NoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private static final Logger log = LogManager.getLogger(NoteController.class);

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getNotesByPatientId(@PathVariable Integer patientId) {
        log.info("Received request to get all notes for patient with ID {}", patientId);
        Optional<List<String>> notes = noteService.getNotesByPatientId(patientId);
        if (notes.isPresent()) {
            log.debug("Found {} notes for patient ID {}", notes.get().size(), patientId);
            return ResponseEntity.ok(notes.get());
        } else {
            log.debug("No notes found for patient ID {}", patientId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<?> addNoteToPatient(@PathVariable Integer patientId, @RequestBody String newNote) {
        log.info("Received request to add note to patient with ID {}", patientId);
        boolean added = noteService.addNoteToPatient(patientId, newNote);
        if (added) {
            log.info("Note added successfully to patient ID {}", patientId);
            return ResponseEntity.ok("Note added successfully");
        } else {
            log.warn("Failed to add note: patient with ID {} not found", patientId);
            return ResponseEntity.notFound().build();
        }
    }

}
