package com.securepass.analyzer.service;

import org.springframework.stereotype.Service;

@Service
public class EntropyService {

    public double estimate(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int characterPool = 0;
        if (password.matches(".*[a-z].*")) {
            characterPool += 26;
        }
        if (password.matches(".*[A-Z].*")) {
            characterPool += 26;
        }
        if (password.matches(".*\\d.*")) {
            characterPool += 10;
        }
        if (password.matches(".*[^a-zA-Z0-9].*")) {
            characterPool += 33;
        }

        if (characterPool == 0) {
            return 0;
        }

        double entropy = password.length() * (Math.log(characterPool) / Math.log(2));
        return Math.round(entropy * 10.0) / 10.0;
    }
}
