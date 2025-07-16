package com.openclassrooms.medilabo.diabetesReportService.service;

import com.openclassrooms.medilabo.diabetesReportService.dto.PatientDemographicsDto;
import com.openclassrooms.medilabo.diabetesReportService.enums.RiskLevel;
import com.openclassrooms.medilabo.diabetesReportService.enums.TriggerTerm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Service for generating diabetes risk reports for patients.
 */
@Service
public class DiabetesReportService {

    private static final Logger log = LogManager.getLogger(DiabetesReportService.class);

    private final WebClient webClient;

    /**
     * Constructor for DiabetesReportService.
     *
     * @param webClient the WebClient used to communicate with external services
     */
    public DiabetesReportService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Generates a diabetes risk report for a given patient.
     *
     * @param patientId the ID of the patient
     * @return the risk level as a String
     */
    public String generateReport(Integer patientId) {
        log.info("Generating diabetes risk report for patient ID {}", patientId);

        PatientDemographicsDto patient = webClient.get()
                .uri("/api/patients/{id}/demographics", patientId)
                .retrieve()
                .bodyToMono(PatientDemographicsDto.class)
                .block();
        log.debug("Retrieved patient demographics: gender={}, dateOfBirth={}", patient.getGender(), patient.getDateOfBirth());

        List<String> notes = webClient.get()
                .uri("/api/notes/patient/{id}", patientId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        if (notes == null || notes.isEmpty()) {
            log.warn("No notes found for patient ID {}", patientId);
            return RiskLevel.NONE.name();
        }
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

    /**
     * Calculates the age of the patient based on their date of birth.
     *
     * @param birthDate the date of birth of the patient
     * @return the age in years
     */
    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Counts the number of unique trigger terms found in the patient's notes.
     *
     * @param notes the list of notes for the patient
     * @return the count of trigger terms
     */
    private int countTriggerTerms(List<String> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (String note : notes) {
            String content = note.toLowerCase();
            Set<TriggerTerm> foundInNote = new HashSet<>();

            for (TriggerTerm trigger : TriggerTerm.values()) {
                String keyword = trigger.getTerm();
                if (content.contains(keyword)) {
                    log.debug("Found trigger term '{}'", keyword);
                    foundInNote.add(trigger);
                }
            }
            count += foundInNote.size();
        }
        return count;
    }

    /**
     * Calculates the diabetes risk level based on age, gender, and trigger term count.
     *
     * @param age the age of the patient
     * @param gender the gender of the patient
     * @param triggerCount the number of trigger terms found
     * @return the calculated RiskLevel
     */
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

    /**
     * Determines the risk level for adult patients (age >= 30).
     *
     * @param triggerCount the number of trigger terms found
     * @return the RiskLevel for adults
     */
    private RiskLevel riskForAdult(int triggerCount) {
        if (triggerCount <= 5) return RiskLevel.BORDERLINE;
        if (triggerCount <= 7) return RiskLevel.IN_DANGER;
        return RiskLevel.EARLY_ONSET;
    }

    /**
     * Determines the risk level for patients under 30 years old.
     *
     * @param triggerCount the number of trigger terms found
     * @param gender the gender of the patient
     * @return the RiskLevel for patients under 30
     */
    private RiskLevel riskForUnder30(int triggerCount, String gender) {
        switch (gender) {
            case "M":
                if (triggerCount == 2) return RiskLevel.BORDERLINE;
                if (triggerCount <= 4) return RiskLevel.IN_DANGER;
                return RiskLevel.EARLY_ONSET;
            case "F":
                if (triggerCount <= 3) return RiskLevel.BORDERLINE;
                if (triggerCount <= 6) return RiskLevel.IN_DANGER;
                return RiskLevel.EARLY_ONSET;
            default:
                log.warn("Unknown gender '{}', defaulting to NONE risk.", gender);
                return RiskLevel.NONE;
        }
    }
}