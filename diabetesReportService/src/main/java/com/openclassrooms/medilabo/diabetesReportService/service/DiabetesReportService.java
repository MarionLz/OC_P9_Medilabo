package com.openclassrooms.medilabo.diabetesReportService.service;

import com.openclassrooms.medilabo.diabetesReportService.Dto.PatientDto;
import com.openclassrooms.medilabo.diabetesReportService.enums.RiskLevel;
import com.openclassrooms.medilabo.diabetesReportService.enums.TriggerTerm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DiabetesReportService {

    private static final Logger log = LogManager.getLogger(DiabetesReportService.class);

    private final WebClient webClient;

    public DiabetesReportService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateReport(Integer patientId) {
        log.info("Generating diabetes risk report for patient ID {}", patientId);

        // 1. Récupérer les infos du patient
        PatientDto patient = webClient.get()
                .uri("/patients/{id}/demographics", patientId)
                .retrieve()
                .bodyToMono(PatientDto.class)
                .block(); // bloquant ici volontairement pour simplifier
        log.debug("Retrieved patient demographics: gender={}, dateOfBirth={}", patient.getGender(), patient.getDateOfBirth());

        // 2. Récupérer les notes du patient
        List<String> notes = webClient.get()
                .uri("/notes/patient/{id}", patientId)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block(); // idem
        log.debug("Retrieved {} note(s) for patient ID {}", notes.size(), patientId);

        int age = calculateAge(patient.getDateOfBirth());
        log.debug("Patient age calculated: {}", age);

        int triggerCount = countTriggerTerms(notes);
        log.debug("Trigger terms count: {}", triggerCount);

        String gender = patient.getGender();

        RiskLevel riskLevel = calculateRiskLevel(age, gender, triggerCount);
        log.info("Calculated risk level: {}", riskLevel);

        return riskLevel.name();
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private int countTriggerTerms(List<String> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (String note : notes) {
            String content = note.toLowerCase();

            for (TriggerTerm trigger : TriggerTerm.values()) {
                String keyword = trigger.getTerm();
                int index = content.indexOf(keyword);
                while (index != -1) {
                    count++;
                    index = content.indexOf(keyword, index + keyword.length());
                }
            }
        }
        return count;
    }

    private RiskLevel calculateRiskLevel(int age, String gender, int triggerCount) {
        if (triggerCount <= 1) {
            return RiskLevel.NONE;
        }
        if (age >= 30) {
            return riskForAdult(triggerCount);
        } else {
            return riskForUnder30(triggerCount, gender);
        }
    }

    private RiskLevel riskForAdult(int triggerCount) {
        if (triggerCount <= 5) return RiskLevel.BORDERLINE;
        if (triggerCount <= 7) return RiskLevel.IN_DANGER;
        return RiskLevel.EARLY_ONSET;
    }

    private RiskLevel riskForUnder30(int triggerCount, String gender) {
        String normalizedGender = gender.toLowerCase();
        switch (normalizedGender) {
            case "male":
                if (triggerCount == 2) return RiskLevel.BORDERLINE;
                if (triggerCount <= 4) return RiskLevel.IN_DANGER;
                return RiskLevel.EARLY_ONSET;
            case "female":
                if (triggerCount <= 3) return RiskLevel.BORDERLINE;
                if (triggerCount <= 6) return RiskLevel.IN_DANGER;
                return RiskLevel.EARLY_ONSET;
            default:
                log.warn("Unknown gender '{}', defaulting to NONE risk.", gender);
                return RiskLevel.NONE;
        }
    }
}
