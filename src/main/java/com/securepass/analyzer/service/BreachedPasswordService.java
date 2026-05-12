package com.securepass.analyzer.service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class BreachedPasswordService {

    private final Map<String, Boolean> breachedPasswords = new HashMap<>();

    @PostConstruct
    void loadBreachedPasswords() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/breached-passwords.txt");
        if (!resource.exists()) {
            return;
        }

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String password = line.trim().toLowerCase();
                if (!password.isBlank()) {
                    breachedPasswords.put(password, Boolean.TRUE);
                }
            }
        }
    }

    public boolean isBreached(String password) {
        return password != null && breachedPasswords.containsKey(password.toLowerCase());
    }
}
