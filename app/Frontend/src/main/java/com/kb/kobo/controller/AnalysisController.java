package com.kb.kobo.controller;

import com.kb.kobo.entity.AnalysisResult;
import com.kb.kobo.entity.Document;
import com.kb.kobo.entity.Question;
import com.kb.kobo.service.AnalysisService;
import com.kb.kobo.service.DocumentService;
import com.kb.kobo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/analysis")
public class AnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

    private final AnalysisService analysisService;
    private final DocumentService documentService;
    private final QuestionService questionService;

    @Autowired
    public AnalysisController(AnalysisService analysisService, DocumentService documentService, QuestionService questionService) {
        this.analysisService = analysisService;
        this.documentService = documentService;
        this.questionService = questionService;
    }

    @PostMapping("/result")
    public ResponseEntity<AnalysisResult> saveAnalysisResult(
            @RequestParam Long documentId,
            @RequestParam String questionContent,
            @RequestParam String result) {

        Document document = documentService.findDocumentById(documentId)
                .orElseThrow(() -> new RuntimeException("문서를 찾을 수 없습니다."));

        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setDocument(document);
        analysisResult.setQuestionContent(questionContent);
        analysisResult.setResult(result);

        AnalysisResult savedResult = analysisService.saveAnalysisResult(analysisResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResult);
    }


    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeDocument(
            @RequestParam(required = false) Long documentId,
            @RequestParam Long questionId) {

        try {
            Question question = questionService.findQuestionById(questionId)
                    .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));

            if (documentId != null) {
                Document document = documentService.findDocumentById(documentId)
                        .orElseThrow(() -> new RuntimeException("문서를 찾을 수 없습니다."));

                Path filePath = Paths.get(document.getFilePath());
                byte[] fileContent = Files.readAllBytes(filePath);
                String fileContentBase64 = java.util.Base64.getEncoder().encodeToString(fileContent);

                HttpClient client = HttpClient.newHttpClient();
                String requestBody = String.format(
                        "{\"file\":\"%s\",\"question\":\"%s\"}",
                        fileContentBase64, question.getContent());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:5000/analyze"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String responseBody = response.body();

                    AnalysisResult analysisResult = new AnalysisResult();
                    analysisResult.setDocument(document);
                    analysisResult.setQuestionContent(question.getContent());
                    analysisResult.setResult(responseBody);

                    analysisService.saveAnalysisResult(analysisResult);

                    return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
                } else {
                    String errorMessage = String.format("AI 서비스 호출 실패: HTTP %d %s", response.statusCode(), response.body());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }
            } else {
                String defaultText = "기본 텍스트";

                HttpClient client = HttpClient.newHttpClient();
                String requestBody = String.format(
                        "{\"file\":\"%s\",\"question\":\"%s\"}",
                        java.util.Base64.getEncoder().encodeToString(defaultText.getBytes()), question.getContent());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:5000/analyze"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String responseBody = response.body();

                    AnalysisResult analysisResult = new AnalysisResult();
                    analysisResult.setQuestionContent(question.getContent());
                    analysisResult.setResult(responseBody);

                    analysisService.saveAnalysisResult(analysisResult);

                    return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
                } else {
                    String errorMessage = String.format("AI 서비스 호출 실패: HTTP %d %s", response.statusCode(), response.body());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }
            }
        } catch (IOException e) {
            logger.error("파일 처리 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("분석 오류 발생");
        }
    }



    @GetMapping("/result/{id}")
    public ResponseEntity<AnalysisResult> getAnalysisResultById(@PathVariable Long id) {
        return analysisService.findAnalysisResultById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 분석 결과 조회
    @GetMapping("/results/document/{documentId}")
    public ResponseEntity<List<AnalysisResult>> getAnalysisResultsByDocument(@PathVariable Long documentId) {
        List<AnalysisResult> results = analysisService.findAnalysisResultsByDocumentId(documentId);
        return results.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(results);
    }

    @DeleteMapping("/result/{id}")
    public ResponseEntity<Void> deleteAnalysisResult(@PathVariable Long id) {
        if (analysisService.findAnalysisResultById(id).isPresent()) {
            analysisService.deleteAnalysisResultById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
