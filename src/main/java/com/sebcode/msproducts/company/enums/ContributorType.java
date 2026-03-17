package com.sebcode.msproducts.company.enums;

public enum ContributorType {
    NATURAL_PERSON("Persona Natural"),
    NON_RESIDENT("No Domiciliado"),
    STATE_ENTITY("Entidad del Estado"),
    LEGAL_ENTITY("Persona Juridica");

    private final String description;

    ContributorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
