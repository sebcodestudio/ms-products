package com.sebcode.msproducts.company.enums;

public enum TaxRegime {
    RUS("Régimen Único Simplificado"),
    RER("Régimen Especial de Renta"),
    GENERAL("Régimen General"),
    MYPE("MYPE Tributario");

    private final String description;

    TaxRegime(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}