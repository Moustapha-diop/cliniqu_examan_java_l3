package com.example.clinique.model.enums;

public enum GroupeSanguin {
    A_POSITIF("A+"), A_NEGATIF("A-"),
    B_POSITIF("B+"), B_NEGATIF("B-"),
    AB_POSITIF("AB+"), AB_NEGATIF("AB-"),
    O_POSITIF("O+"), O_NEGATIF("O-");

    private final String label;
    GroupeSanguin(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() { return label; }
}
