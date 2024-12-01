package com.kb.kobo.document.controller;

import com.kb.kobo.document.domain.Document;
import com.kb.kobo.document.repository.DocumentRepository;
import com.kb.kobo.user.domain.User;
import com.kb.kobo.document.service.DocumentService;
import com.kb.kobo.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/documents")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService, UserRepository userRepository, DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return new ResponseEntity<>("업로드할 파일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            Document document = documentService.uploadAndSaveDocument(file, user.getUsername());
            return new ResponseEntity<>("파일 업로드를 성공하였습니다. 문서 ID: " + document.getDocumentId(), HttpStatus.OK);
        } catch (IOException e) {
            logger.error("파일 업로드 실패: {}", e.getMessage());
            return new ResponseEntity<>("파일 업로드를 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Document>> listDocuments(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Long userId = currentUser.getId();

        List<Document> documents = documentRepository.findByUserId(userId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        Optional<Document> document = documentRepository.findByDocumentId(id);
        return document.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return new ResponseEntity<>("문서 삭제를 성공하였습니다.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
