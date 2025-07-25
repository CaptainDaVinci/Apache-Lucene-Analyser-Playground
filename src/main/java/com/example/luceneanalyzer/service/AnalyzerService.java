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

    private static final Map<String, String> ANALYZER_CLASSES = Map.ofEntries(
            Map.entry("standard", "org.apache.lucene.analysis.standard.StandardAnalyzer"),
            Map.entry("english", "org.apache.lucene.analysis.en.EnglishAnalyzer"),
            Map.entry("simple", "org.apache.lucene.analysis.core.SimpleAnalyzer"),
            Map.entry("classic", "org.apache.lucene.analysis.classic.ClassicAnalyzer"),
            Map.entry("keyword", "org.apache.lucene.analysis.core.KeywordAnalyzer"),
            Map.entry("whitespace", "org.apache.lucene.analysis.core.WhitespaceAnalyzer"),
            Map.entry("stop", "org.apache.lucene.analysis.core.StopAnalyzer"),
            // Language-specific analyzers
            Map.entry("arabic", "org.apache.lucene.analysis.ar.ArabicAnalyzer"),
            Map.entry("armenian", "org.apache.lucene.analysis.hy.ArmenianAnalyzer"),
            Map.entry("basque", "org.apache.lucene.analysis.eu.BasqueAnalyzer"),
            Map.entry("bengali", "org.apache.lucene.analysis.bn.BengaliAnalyzer"),
            Map.entry("brazilian", "org.apache.lucene.analysis.br.BrazilianAnalyzer"),
            Map.entry("bulgarian", "org.apache.lucene.analysis.bg.BulgarianAnalyzer"),
            Map.entry("catalan", "org.apache.lucene.analysis.ca.CatalanAnalyzer"),
            Map.entry("cjk", "org.apache.lucene.analysis.cjk.CJKAnalyzer"),
            Map.entry("chinese", "org.apache.lucene.analysis.cjk.CJKAnalyzer"),
            Map.entry("czech", "org.apache.lucene.analysis.cz.CzechAnalyzer"),
            Map.entry("danish", "org.apache.lucene.analysis.da.DanishAnalyzer"),
            Map.entry("dutch", "org.apache.lucene.analysis.nl.DutchAnalyzer"),
            Map.entry("finnish", "org.apache.lucene.analysis.fi.FinnishAnalyzer"),
            Map.entry("french", "org.apache.lucene.analysis.fr.FrenchAnalyzer"),
            Map.entry("galician", "org.apache.lucene.analysis.gl.GalicianAnalyzer"),
            Map.entry("german", "org.apache.lucene.analysis.de.GermanAnalyzer"),
            Map.entry("greek", "org.apache.lucene.analysis.el.GreekAnalyzer"),
            Map.entry("hindi", "org.apache.lucene.analysis.hi.HindiAnalyzer"),
            Map.entry("hungarian", "org.apache.lucene.analysis.hu.HungarianAnalyzer"),
            Map.entry("indonesian", "org.apache.lucene.analysis.id.IndonesianAnalyzer"),
            Map.entry("irish", "org.apache.lucene.analysis.ga.IrishAnalyzer"),
            Map.entry("italian", "org.apache.lucene.analysis.it.ItalianAnalyzer"),
            Map.entry("latvian", "org.apache.lucene.analysis.lv.LatvianAnalyzer"),
            Map.entry("norwegian", "org.apache.lucene.analysis.no.NorwegianAnalyzer"),
            Map.entry("persian", "org.apache.lucene.analysis.fa.PersianAnalyzer"),
            Map.entry("portuguese", "org.apache.lucene.analysis.pt.PortugueseAnalyzer"),
            Map.entry("romanian", "org.apache.lucene.analysis.ro.RomanianAnalyzer"),
            Map.entry("russian", "org.apache.lucene.analysis.ru.RussianAnalyzer"),
            Map.entry("sorani", "org.apache.lucene.analysis.ckb.SoraniAnalyzer"),
            Map.entry("spanish", "org.apache.lucene.analysis.es.SpanishAnalyzer"),
            Map.entry("swedish", "org.apache.lucene.analysis.sv.SwedishAnalyzer"),
            Map.entry("thai", "org.apache.lucene.analysis.th.ThaiAnalyzer"),
            Map.entry("turkish", "org.apache.lucene.analysis.tr.TurkishAnalyzer")
    );

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
        String key = name.toLowerCase(Locale.ROOT);
        String className = ANALYZER_CLASSES.get(key);
        if (className == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            return (Analyzer) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to instantiate analyzer: " + className, e);
        }
    }

    public Set<String> supportedAnalyzers() {
        return ANALYZER_CLASSES.keySet();
    }
} 