package com.securepass.analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordAnalysisRequest(
        @NotBlank(message = "Password is required")
        @Size(max = 128, message = "Password must be 128 characters or fewer")
        String password
) {
}
