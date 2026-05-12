package com.securepass.analyzer.model;

public enum StrengthLevel {
    VERY_WEAK("Very Weak"),
    WEAK("Weak"),
    MEDIUM("Medium"),
    STRONG("Strong"),
    VERY_STRONG("Very Strong");

    private final String label;

    StrengthLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
