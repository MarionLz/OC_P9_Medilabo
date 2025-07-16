package com.openclassrooms.medilabo.notesService.service;

import com.openclassrooms.medilabo.notesService.model.Note;
import com.openclassrooms.medilabo.notesService.repository.NoteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing patient notes.
 */
@Service
public class NoteService {

    private static final Logger log = LogManager.getLogger(NoteService.class);

    private final NoteRepository noteRepository;

    /**
     * Constructor for NoteService.
     *
     * @param noteRepository the repository for note operations
     */
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Retrieves all notes for a given patient by their ID.
     *
     * @param patientId the ID of the patient
     * @return an Optional containing a list of note contents, or empty if none found
     */
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

    /**
     * Adds a new note to a patient.
     *
     * @param patientId the ID of the patient
     * @param newNote the content of the new note
     * @param patientName the name or identifier of the patient
     * @return true if the note was added successfully, false otherwise
     */
    public boolean addNoteToPatient(Integer patientId, String newNote, String patientName) {
        log.info("Attempting to add new note to patient ID {}", patientId);
        try {
            Note newNoteDocument = new Note();
            newNoteDocument.setPatientId(patientId);
            newNoteDocument.setNote(newNote);
            newNoteDocument.setPatient(patientName);
            noteRepository.save(newNoteDocument);
            log.info("Note added successfully for patient ID {}", patientId);
            log.debug("Content of the note: {}", newNoteDocument.getNote());
            return true;
        } catch (Exception e) {
            log.error("Failed to add note for patient ID {}: {}", patientId, e.getMessage());
            return false;
        }
    }

}