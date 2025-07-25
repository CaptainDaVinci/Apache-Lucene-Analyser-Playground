package com.example.luceneanalyzer.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class AnalyzerService {

    public List<String> analyzeText(String analyzerName, String text) throws IOException {
        Analyzer analyzer = createAnalyzer(analyzerName);
        if (analyzer == null) {
            throw new IllegalArgumentException("Unsupported analyzer: " + analyzerName);
        }
        List<String> result = new ArrayList<>();
        try (TokenStream tokenStream = analyzer.tokenStream("field", text)) {
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.add(attr.toString());
            }
            tokenStream.end();
        }
        return result;
    }

    private Analyzer createAnalyzer(String name) {
        if (name == null) return null;
        switch (name.toLowerCase()) {
            case "standard":
                return new StandardAnalyzer();
            case "english":
                return new EnglishAnalyzer();
            case "simple":
                return new SimpleAnalyzer();
            default:
                return null;
        }
    }

    public Set<String> supportedAnalyzers() {
        return new HashSet<>(Arrays.asList("standard", "english", "simple"));
    }
} 