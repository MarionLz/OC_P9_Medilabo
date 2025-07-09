package com.openclassrooms.medilabo.notesService.controller;

import com.openclassrooms.medilabo.notesService.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getNotesByPatientId(@PathVariable Integer patientId) {
        Optional<List<String>> notes = noteService.getNotesByPatientId(patientId);
        return notes.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<?> addNoteToPatient(@PathVariable Integer patientId, @RequestBody String newNote) {
        boolean added = noteService.addNoteToPatient(patientId, newNote);
        if (added) {
            return ResponseEntity.ok("Note ajoutée avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
