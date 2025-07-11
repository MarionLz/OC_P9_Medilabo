package com.openclassrooms.medilabo.notesService.service;

import com.openclassrooms.medilabo.notesService.model.Note;
import com.openclassrooms.medilabo.notesService.repository.NoteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private static final Logger log = LogManager.getLogger(NoteService.class);

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Optional<List<String>> getNotesByPatientId(Integer patId) {
        log.info("Fetching notes for patient ID {}", patId);
        Optional<Note> note = noteRepository.findByPatientId(patId);
        return note.map(Note::getNotes);
    }

    public boolean addNoteToPatient(Integer patientId, String newNote) {
        log.info("Attempting to add new note to patient ID {}", patientId);
        log.debug("Note content to add: {}", newNote);
        Optional<Note> optionalNote = noteRepository.findByPatientId(patientId);
        if (optionalNote.isPresent()) {
            Note existingNote = optionalNote.get();
            existingNote.getNotes().add(newNote);
            noteRepository.save(existingNote);
            log.info("Note added successfully for patient ID {}", patientId);
            return true;
        } else {
            return false;
        }
    }
}
