package com.securepass.analyzer.controller;

import com.securepass.analyzer.dto.PasswordAnalysisRequest;
import com.securepass.analyzer.service.PasswordSuggestionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    private final PasswordSuggestionService suggestionService;

    public PageController(PasswordSuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/analyzer")
    public String analyzer(Model model) {
        model.addAttribute("analysisRequest", new PasswordAnalysisRequest(""));
        model.addAttribute("suggestedPassword", suggestionService.generate(14));
        return "analyzer";
    }

    @PostMapping("/analyzer")
    public String analyzerSubmit(
            @Valid @ModelAttribute("analysisRequest") PasswordAnalysisRequest request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("suggestedPassword", suggestionService.generate(14));
            return "analyzer";
        }
        return "redirect:/analyzer";
    }
}
