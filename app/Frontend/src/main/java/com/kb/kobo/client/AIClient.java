// AIClient
package com.kb.kobo.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import org.json.JSONObject;

public class AIClient {

    public String analyzeDocument(String documentFilePath, String question) throws Exception {
        // 파일 읽기
        Path path = Path.of(documentFilePath);
        byte[] fileContent = Files.readAllBytes(path);
        String fileContentBase64 = Base64.getEncoder().encodeToString(fileContent);

        // 요청 본문 생성
        JSONObject json = new JSONObject();
        json.put("file", fileContentBase64);
        json.put("question", question);

        // HttpClient 생성
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/analysis/analyze"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        // 요청 보내기 및 응답 받기
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("AI 서버 호출 실패: " + response.statusCode());
        }
    }

    public static void main(String[] args) {
        try {
            AIClient aiClient = new AIClient();
            String result = aiClient.analyzeDocument("path/to/document.pdf", "이 문서의 요점은 무엇인가요?");
            System.out.println("AI 분석 결과: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
