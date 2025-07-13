package com.openclassrooms.medilabo.diabetesReportService.enums;

public enum TriggerTerm {
    HEMOGLOBINE_A1C("hémoglobine a1c"),
    MICROALBUMINE("microalbumine"),
    TAILLE("taille"),
    POIDS("poids"),
    FUMEUR("fumeur"),
    FUMEUSE("fumeuse"),
    ANORMAL("anormal"),
    ANORMALE("anormale"),
    ANORMALES("anormales"),
    CHOLESTEROL("cholestérol"),
    VERTIGES("vertiges"),
    RECHUTE("rechute"),
    REACTION("réaction"),
    ANTICORPS("anticorps");

    private final String term;

    TriggerTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}