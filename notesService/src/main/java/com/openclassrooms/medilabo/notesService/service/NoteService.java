package com.openclassrooms.medilabo.notesService.service;

import com.openclassrooms.medilabo.notesService.model.Note;
import com.openclassrooms.medilabo.notesService.repository.NoteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private static final Logger log = LogManager.getLogger(NoteService.class);

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Optional<List<String>> getNotesByPatientId(Integer patientId) {
        log.info("Fetching notes for patient ID {}", patientId);
        List<Note> notes = noteRepository.findAllByPatientId(patientId);
        if (notes.isEmpty()) {
            return Optional.empty();
        }
        List<String> noteContents = notes.stream()
                .map(Note::getNote)
                .collect(Collectors.toList());
        return Optional.of(noteContents);
    }

    public boolean addNoteToPatient(Integer patientId, String newNote, String patientName) {
        log.info("Attempting to add new note to patient ID {}", patientId);
        try {
            Note newNoteDocument = new Note();
            newNoteDocument.setPatientId(patientId);
            newNoteDocument.setNote(newNote);
            newNoteDocument.setPatient(patientName);
            noteRepository.save(newNoteDocument);
            log.info("Note added successfully for patient ID {}", patientId);
            return true;
        } catch (Exception e) {
            log.error("Failed to add note for patient ID {}: {}", patientId, e.getMessage());
            return false;
        }
    }

}
