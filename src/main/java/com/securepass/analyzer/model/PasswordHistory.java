package com.securepass.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.Instant;

@Entity
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String passwordFingerprint;

    @Column(nullable = false)
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StrengthLevel strengthLevel;

    @Column(nullable = false)
    private double entropy;

    @Column(nullable = false)
    private boolean weakPassword;

    @Column(nullable = false)
    private boolean dictionaryPattern;

    @Column(nullable = false)
    private boolean breachedPassword;

    @Column(nullable = false, updatable = false)
    private Instant analyzedAt;

    @PrePersist
    void prePersist() {
        analyzedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setPasswordFingerprint(String passwordFingerprint) {
        this.passwordFingerprint = passwordFingerprint;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStrengthLevel(StrengthLevel strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public void setWeakPassword(boolean weakPassword) {
        this.weakPassword = weakPassword;
    }

    public void setDictionaryPattern(boolean dictionaryPattern) {
        this.dictionaryPattern = dictionaryPattern;
    }

    public void setBreachedPassword(boolean breachedPassword) {
        this.breachedPassword = breachedPassword;
    }
}
