package com.example.luceneanalyzer.controller;

import com.example.luceneanalyzer.service.AnalyzerService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/analyze")
@Validated
public class AnalyzerController {

    private final AnalyzerService analyzerService;

    public AnalyzerController(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping
    public ResponseEntity<?> analyze(@RequestParam(name = "analyzer", defaultValue = "standard") String analyzer,
                                     @RequestBody @NotBlank String text) {
        try {
            List<String> tokens = analyzerService.analyzeText(analyzer, text);
            Map<String, Object> response = new HashMap<>();
            response.put("analyzer", analyzer);
            response.put("tokens", tokens);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error analyzing text");
        }
    }

    @GetMapping("/supported")
    public Set<String> supportedAnalyzers() {
        return analyzerService.supportedAnalyzers();
    }
} 