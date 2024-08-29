package com.kb.kobo.controller;

import com.kb.kobo.entity.Question;
import com.kb.kobo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Question> submitQuestion(
            @RequestParam("email") String email,
            @RequestParam("content") String content,
            @RequestParam(value = "documentId", required = false) String documentId) {

        // 입력된 이메일, 콘텐츠 및 문서 ID를 콘솔에 출력하여 디버깅
        System.out.println("Received submitQuestion request with email: " + email);
        System.out.println("Received submitQuestion request with content: " + content);
        System.out.println("Received submitQuestion request with documentId: " + documentId);

        try {
            // 이메일을 사용하여 질문 제출
            Question question = questionService.submitQuestion(email, content, documentId);
            System.out.println("Question submitted successfully with ID: " + question.getQuestionId());
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        } catch (Exception e) {
            // 예외 발생 시 콘솔에 출력
            System.out.println("Error submitting question: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionService.findQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return new ResponseEntity<>("질문이 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("질문 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
