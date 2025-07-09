package com.openclassrooms.medilabo.notesService.service;

import com.openclassrooms.medilabo.notesService.model.Note;
import com.openclassrooms.medilabo.notesService.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Optional<List<String>> getNotesByPatientId(Integer patId) {
        Optional<Note> note = noteRepository.findByPatientId(patId);
        return note.map(Note::getNotes);
    }

    public boolean addNoteToPatient(Integer patId, String newNote) {
        Optional<Note> optionalNote = noteRepository.findByPatientId(patId);
        if (optionalNote.isPresent()) {
            Note existingNote = optionalNote.get();
            existingNote.getNotes().add(newNote);
            noteRepository.save(existingNote);
            return true;
        } else {
            return false;
        }
    }
}
