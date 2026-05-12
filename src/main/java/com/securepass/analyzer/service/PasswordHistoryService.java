package com.securepass.analyzer.service;

import com.securepass.analyzer.dto.PasswordAnalysisResponse;
import com.securepass.analyzer.model.PasswordHistory;
import com.securepass.analyzer.repository.PasswordHistoryRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.stereotype.Service;

@Service
public class PasswordHistoryService {

    private final PasswordHistoryRepository repository;

    public PasswordHistoryService(PasswordHistoryRepository repository) {
        this.repository = repository;
    }

    public void record(String password, PasswordAnalysisResponse response) {
        PasswordHistory history = new PasswordHistory();
        history.setPasswordFingerprint(hash(password));
        history.setScore(response.score());
        history.setStrengthLevel(response.strength());
        history.setEntropy(response.entropy());
        history.setWeakPassword(response.weakPassword());
        history.setDictionaryPattern(response.dictionaryPattern());
        history.setBreachedPassword(response.breachedPassword());
        repository.save(history);
    }

    private String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
