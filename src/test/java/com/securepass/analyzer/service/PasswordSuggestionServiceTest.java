package com.securepass.analyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PasswordSuggestionServiceTest {

    private final PasswordSuggestionService suggestionService = new PasswordSuggestionService();

    @Test
    void generatesPasswordWithRequiredCharacterGroups() {
        String password = suggestionService.generate(14);

        assertThat(password).hasSize(14);
        assertThat(password).containsPattern("[A-Z]");
        assertThat(password).containsPattern("[a-z]");
        assertThat(password).containsPattern("\\d");
        assertThat(password).containsPattern("[^a-zA-Z0-9]");
    }
}
