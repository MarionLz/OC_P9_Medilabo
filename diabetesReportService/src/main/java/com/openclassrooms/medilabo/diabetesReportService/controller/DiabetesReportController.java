package com.openclassrooms.medilabo.diabetesReportService.controller;

import com.openclassrooms.medilabo.diabetesReportService.service.DiabetesReportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diabetes-report")
public class DiabetesReportController {

    private static final Logger log = LogManager.getLogger(DiabetesReportController.class);

    private final DiabetesReportService diabetesReportService;

    public DiabetesReportController(DiabetesReportService diabetesReportService) {
        this.diabetesReportService = diabetesReportService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<String> getDiabetesReport(@PathVariable Integer patientId) {
        log.info("Received request to get diabetes report");
        String report = diabetesReportService.generateReport(patientId);
        log.debug("Generated diabetes report: {}", report);
        return ResponseEntity.ok(report);
    }
}
