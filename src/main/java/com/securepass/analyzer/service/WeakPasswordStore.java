package com.securepass.analyzer.service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class WeakPasswordStore {

    private final Map<String, Boolean> weakPasswords = new HashMap<>();

    @PostConstruct
    void loadCommonPasswords() {
        List.of(
                "123456", "123456789", "password", "admin", "qwerty", "welcome",
                "letmein", "iloveyou", "abc123", "password1", "admin123", "111111",
                "dragon", "monkey", "football", "baseball", "login", "master",
                "sunshine", "princess", "qwerty123", "welcome1", "passw0rd")
                .forEach(password -> weakPasswords.put(password, Boolean.TRUE));
    }

    public boolean isWeakPassword(String password) {
        return password != null && weakPasswords.containsKey(password.toLowerCase());
    }
}
