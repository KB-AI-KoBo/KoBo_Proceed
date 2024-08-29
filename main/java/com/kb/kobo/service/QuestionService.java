package com.kb.kobo.service;

import com.kb.kobo.entity.Document;
import com.kb.kobo.entity.Question;
import com.kb.kobo.entity.User;
import com.kb.kobo.repository.QuestionRepository;
import com.kb.kobo.repository.DocumentRepository;
import com.kb.kobo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           DocumentRepository documentRepository,
                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Question submitQuestion(String email, String content, String documentId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Question question = new Question();
        question.setUser(user);
        question.setContent(content);
        question.setDocumentId(documentId); // 문서 ID 설정

        return questionRepository.save(question);
    }


    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new RuntimeException("질문을 찾을 수 없습니다.");
        }
    }
}
