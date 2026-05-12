package com.securepass.analyzer.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PasswordSuggestionService {

    private static final String UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijkmnopqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final String SPECIAL = "!@#$%^&*?";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generate(int length) {
        int safeLength = Math.max(10, Math.min(length, 32));
        List<Character> characters = new ArrayList<>();
        characters.add(randomFrom(UPPER));
        characters.add(randomFrom(LOWER));
        characters.add(randomFrom(DIGITS));
        characters.add(randomFrom(SPECIAL));

        while (characters.size() < safeLength) {
            characters.add(randomFrom(ALL));
        }

        Collections.shuffle(characters, secureRandom);
        StringBuilder builder = new StringBuilder();
        characters.forEach(builder::append);
        return builder.toString();
    }

    private char randomFrom(String source) {
        return source.charAt(secureRandom.nextInt(source.length()));
    }
}
