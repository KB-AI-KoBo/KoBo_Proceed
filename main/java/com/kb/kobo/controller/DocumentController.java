package com.kb.kobo.controller;

import com.kb.kobo.entity.Document;
import com.kb.kobo.entity.User;
import com.kb.kobo.service.DocumentService;
import com.kb.kobo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/documents")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Value("${upload.dir}")
    private String uploadDir;

    private final DocumentService documentService;
    private final UserService userService;

    @Autowired
    public DocumentController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) {

        if (file.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "업로드할 파일이 존재하지 않습니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        System.out.println("Received submitQuestion request with email: " + email);
        System.out.println("Received submitQuestion request with file: " + file);


        try {
            Document document = documentService.uploadAndSaveDocument(file, email);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "파일 업로드를 성공하였습니다.");
            response.put("documentId", document.getDocumentId());
            response.put("filePath", document.getFilePath());  // 파일 경로를 응답에 추가
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("파일 업로드 실패: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "파일 업로드를 실패하였습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping
    public ResponseEntity<List<Document>> listDocuments(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Document> documents = documentService.findDocumentsByUser(currentUser);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable("id") Long id) {
        Optional<Document> document = documentService.findDocumentById(id);
        return document.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") Long id) {
        try {
            documentService.deleteDocument(id);
            return new ResponseEntity<>("문서 삭제를 성공하였습니다.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
