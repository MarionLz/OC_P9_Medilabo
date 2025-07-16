package com.openclassrooms.medilabo.notesService.repository;

import com.openclassrooms.medilabo.notesService.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Note entities in MongoDB.
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, Integer> {

    /**
     * Retrieves all notes associated with a specific patient ID.
     *
     * @param patientId the ID of the patient
     * @return a list of notes for the given patient ID
     */
    List<Note> findAllByPatientId(Integer patientId);
}