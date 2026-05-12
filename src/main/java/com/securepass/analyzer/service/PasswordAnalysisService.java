package com.securepass.analyzer.service;

import com.securepass.analyzer.dto.PasswordAnalysisResponse;
import com.securepass.analyzer.model.StrengthLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordAnalysisService {

    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    private static final Pattern SPECIAL = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern REPEATED = Pattern.compile("(.)\\1{2,}");
    private static final List<String> KEYBOARD_PATTERNS = List.of("qwerty", "asdf", "zxcv", "12345", "09876");

    private final WeakPasswordStore weakPasswordStore;
    private final DictionaryPatternService dictionaryPatternService;
    private final BreachedPasswordService breachedPasswordService;
    private final EntropyService entropyService;
    private final PasswordSuggestionService suggestionService;
    private final PasswordHistoryService historyService;

    public PasswordAnalysisService(
            WeakPasswordStore weakPasswordStore,
            DictionaryPatternService dictionaryPatternService,
            BreachedPasswordService breachedPasswordService,
            EntropyService entropyService,
            PasswordSuggestionService suggestionService,
            PasswordHistoryService historyService) {
        this.weakPasswordStore = weakPasswordStore;
        this.dictionaryPatternService = dictionaryPatternService;
        this.breachedPasswordService = breachedPasswordService;
        this.entropyService = entropyService;
        this.suggestionService = suggestionService;
        this.historyService = historyService;
    }

    @Transactional
    public PasswordAnalysisResponse analyze(String password) {
        String safePassword = password == null ? "" : password;
        List<String> passedChecks = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        int score = 0;

        score += evaluate(safePassword.length() >= 12, 18, "Minimum recommended length reached", "Use at least 12 characters", passedChecks, suggestions);
        score += evaluate(UPPERCASE.matcher(safePassword).find(), 12, "Uppercase letters present", "Add uppercase letters", passedChecks, suggestions);
        score += evaluate(LOWERCASE.matcher(safePassword).find(), 12, "Lowercase letters present", "Add lowercase letters", passedChecks, suggestions);
        score += evaluate(DIGIT.matcher(safePassword).find(), 12, "Digits present", "Add digits", passedChecks, suggestions);
        score += evaluate(SPECIAL.matcher(safePassword).find(), 14, "Special characters present", "Add symbols like #, @, or !", passedChecks, suggestions);

        if (safePassword.length() >= 16) {
            score += 10;
            passedChecks.add("Long password bonus applied");
        }

        if (REPEATED.matcher(safePassword).find()) {
            score -= 12;
            warnings.add("Repeated characters reduce resistance against guessing attacks.");
        } else {
            score += 6;
            passedChecks.add("No obvious repeated character blocks");
        }

        if (hasSequentialPattern(safePassword)) {
            score -= 14;
            warnings.add("Sequential patterns such as abc or 123 are predictable.");
        }

        if (hasKeyboardPattern(safePassword)) {
            score -= 14;
            warnings.add("Keyboard patterns such as qwerty or asdf are easy to guess.");
        }

        boolean weakPassword = weakPasswordStore.isWeakPassword(safePassword);
        if (weakPassword) {
            score -= 35;
            warnings.add("This password is in the common weak-password HashMap and is dangerous.");
        }

        boolean dictionaryPattern = dictionaryPatternService.containsPredictablePattern(safePassword);
        if (dictionaryPattern) {
            score -= 18;
            warnings.add("Password contains predictable dictionary-based patterns.");
        }

        boolean breachedPassword = breachedPasswordService.isBreached(safePassword);
        if (breachedPassword) {
            score -= 30;
            warnings.add("This password exists in previously leaked datasets.");
        }

        double entropy = entropyService.estimate(safePassword);
        if (entropy >= 80) {
            score += 12;
            passedChecks.add("High entropy estimate");
        } else if (entropy < 45) {
            suggestions.add("Increase entropy with length, mixed casing, digits, and symbols");
        }

        score = Math.max(0, Math.min(score, 100));
        StrengthLevel strength = strengthLevel(score);
        if (warnings.isEmpty()) {
            warnings.add("No high-risk patterns detected.");
        }

        PasswordAnalysisResponse response = new PasswordAnalysisResponse(
                score,
                strength,
                entropy,
                weakPassword,
                dictionaryPattern,
                breachedPassword,
                passedChecks,
                warnings,
                suggestions,
                suggestionService.generate(Math.max(14, Math.min(safePassword.length() + 4, 24))));

        historyService.record(safePassword, response);
        return response;
    }

    private int evaluate(boolean condition, int points, String passMessage, String suggestion, List<String> passedChecks, List<String> suggestions) {
        if (condition) {
            passedChecks.add(passMessage);
            return points;
        }
        suggestions.add(suggestion);
        return 0;
    }

    private boolean hasKeyboardPattern(String password) {
        String lower = password.toLowerCase();
        return KEYBOARD_PATTERNS.stream().anyMatch(lower::contains);
    }

    private boolean hasSequentialPattern(String password) {
        String lower = password.toLowerCase();
        for (int index = 0; index < lower.length() - 2; index++) {
            char first = lower.charAt(index);
            char second = lower.charAt(index + 1);
            char third = lower.charAt(index + 2);
            if (second == first + 1 && third == second + 1) {
                return true;
            }
        }
        return false;
    }

    private StrengthLevel strengthLevel(int score) {
        if (score < 25) {
            return StrengthLevel.VERY_WEAK;
        }
        if (score < 45) {
            return StrengthLevel.WEAK;
        }
        if (score < 65) {
            return StrengthLevel.MEDIUM;
        }
        if (score < 85) {
            return StrengthLevel.STRONG;
        }
        return StrengthLevel.VERY_STRONG;
    }
}
