package com.securepass.analyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EntropyServiceTest {

    private final EntropyService entropyService = new EntropyService();

    @Test
    void estimatesHigherEntropyForLongMixedPasswords() {
        double weakEntropy = entropyService.estimate("admin123");
        double strongEntropy = entropyService.estimate("A#d9mX!72LpQ");

        assertThat(strongEntropy).isGreaterThan(weakEntropy);
    }
}
