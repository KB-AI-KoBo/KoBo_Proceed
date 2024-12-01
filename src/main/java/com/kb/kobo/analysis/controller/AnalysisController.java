package com.kb.kobo.analysis.controller;

import com.kb.kobo.analysis.domain.AnalysisResult;
import com.kb.kobo.analysis.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/analysis")
public class AnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/result/{id}")
    public ResponseEntity<AnalysisResult> getAnalysisResultById(@PathVariable Long id) {
        return analysisService.findAnalysisResultById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/results/document/{documentId}")
    public ResponseEntity<List<AnalysisResult>> getAnalysisResultsByDocument(@PathVariable Long documentId) {
        List<AnalysisResult> results = analysisService.findAnalysisResultsByDocumentId(documentId);
        return results.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(results);
    }

    @DeleteMapping("/result/{id}")
    public ResponseEntity<Void> deleteAnalysisResult(@PathVariable Long id) {
        if (analysisService.findAnalysisResultById(id).isPresent()) {
            analysisService.deleteAnalysisResultById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
