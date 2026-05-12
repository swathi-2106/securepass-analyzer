package com.securepass.analyzer.controller;

import com.securepass.analyzer.dto.PasswordAnalysisRequest;
import com.securepass.analyzer.dto.PasswordAnalysisResponse;
import com.securepass.analyzer.service.PasswordAnalysisService;
import com.securepass.analyzer.service.PasswordSuggestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordAnalysisController {

    private final PasswordAnalysisService analysisService;
    private final PasswordSuggestionService suggestionService;

    public PasswordAnalysisController(PasswordAnalysisService analysisService, PasswordSuggestionService suggestionService) {
        this.analysisService = analysisService;
        this.suggestionService = suggestionService;
    }

    @PostMapping("/api/analyze")
    public ResponseEntity<PasswordAnalysisResponse> analyze(@Valid @RequestBody PasswordAnalysisRequest request) {
        return ResponseEntity.ok(analysisService.analyze(request.password()));
    }

    @PostMapping("/api/suggest")
    public ResponseEntity<String> suggest(@RequestParam(defaultValue = "14") int length) {
        int boundedLength = Math.max(10, Math.min(length, 32));
        return ResponseEntity.ok(suggestionService.generate(boundedLength));
    }
}
