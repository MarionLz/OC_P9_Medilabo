package com.openclassrooms.medilabo.diabetesReportService.service;

import com.openclassrooms.medilabo.diabetesReportService.Dto.NoteDto;
import com.openclassrooms.medilabo.diabetesReportService.Dto.PatientDto;
import com.openclassrooms.medilabo.diabetesReportService.controller.DiabetesReportController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DiabetesReportService {

    private static final Logger log = LogManager.getLogger(DiabetesReportService.class);

    private final WebClient webClient;

    public DiabetesReportService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateReport(Integer patientId) {

        // 1. Récupérer les infos du patient
        PatientDto patient = webClient.get()
                .uri("/patients/{id}/demographics", patientId)
                .retrieve()
                .bodyToMono(PatientDto.class)
                .block(); // bloquant ici volontairement pour simplifier

        // 2. Récupérer les notes du patient
        List<NoteDto> notes = webClient.get()
                .uri("/notes/patient/{id}", patientId)
                .retrieve()
                .bodyToFlux(NoteDto.class)
                .collectList()
                .block(); // idem

        // 3. Générer le rapport
        return generateRiskReport(patient, notes);
    }

    private String generateRiskReport(PatientDto patient, List<NoteDto> notes) {
        // Ton algorithme ici (prochaine étape)
        return "Rapport de diabète à générer";
    }
}
