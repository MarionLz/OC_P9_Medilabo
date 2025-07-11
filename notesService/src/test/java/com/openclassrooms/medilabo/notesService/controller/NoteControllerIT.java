package com.openclassrooms.medilabo.notesService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.medilabo.notesService.Dto.NoteRequest;
import com.openclassrooms.medilabo.notesService.model.Note;
import com.openclassrooms.medilabo.notesService.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class NoteControllerIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();

        Note note = new Note();
        note.setPatientId(123);
        note.setNote("Diabète détecté.");
        note.setPatient("John Doe");

        noteRepository.save(note);
    }

    @Test
    void testGetNotesByPatientId_shouldReturnNotes() throws Exception {
        mockMvc.perform(get("/notes/patient/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Diabète détecté."));
    }

    @Test
    void testGetNotesByPatientId_shouldReturnNotFoundWhenNoNotes() throws Exception {
        mockMvc.perform(get("/notes/patient/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddNoteToPatient_shouldAddNote() throws Exception {
        NoteRequest newNote = new NoteRequest("Nouveau suivi médical.", "Jane Doe");

        mockMvc.perform(post("/notes/patient/456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNote)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Note ajoutée avec succès."));
    }
}
