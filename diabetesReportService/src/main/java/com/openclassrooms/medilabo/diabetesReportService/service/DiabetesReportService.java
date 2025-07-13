package com.openclassrooms.medilabo.diabetesReportService.service;

import com.openclassrooms.medilabo.diabetesReportService.controller.DiabetesReportController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DiabetesReportService {

    private static final Logger log = LogManager.getLogger(DiabetesReportService.class);

    public String generateReport() {
        // Logique pour générer le rapport de diabète
        // Pour l'instant, on retourne une chaîne de caractères statique
        return "Rapport de diabète généré avec succès.";
    }
}
