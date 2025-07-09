package com.openclassrooms.medilabo.notesService.repository;

import com.openclassrooms.medilabo.notesService.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends MongoRepository<Note, Integer> {

    Optional<Note> findByPatientId(Integer patientId);
}