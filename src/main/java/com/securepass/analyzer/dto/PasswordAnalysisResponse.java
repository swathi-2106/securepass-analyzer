package com.securepass.analyzer.dto;

import com.securepass.analyzer.model.StrengthLevel;
import java.util.List;

public record PasswordAnalysisResponse(
        int score,
        StrengthLevel strength,
        double entropy,
        boolean weakPassword,
        boolean dictionaryPattern,
        boolean breachedPassword,
        List<String> passedChecks,
        List<String> warnings,
        List<String> suggestions,
        String generatedPassword
) {
}
