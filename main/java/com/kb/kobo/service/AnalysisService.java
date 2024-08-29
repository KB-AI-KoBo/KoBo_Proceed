package com.kb.kobo.service;

import com.kb.kobo.entity.AnalysisResult;
import com.kb.kobo.entity.Document;
import com.kb.kobo.repository.AnalysisResultRepository;
import com.kb.kobo.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AnalysisService {

    private final AnalysisResultRepository analysisResultRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public AnalysisService(AnalysisResultRepository analysisResultRepository, DocumentRepository documentRepository) {
        this.analysisResultRepository = analysisResultRepository;
        this.documentRepository = documentRepository;
    }

    public AnalysisResult saveAnalysisResult(AnalysisResult analysisResult) {
        return analysisResultRepository.save(analysisResult);
    }

    public AnalysisResult saveAnalysisResultByDocument(Long documentId, AnalysisResult analysisResult) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            analysisResult.setDocument(document); // 수정된 부분
            return analysisResultRepository.save(analysisResult);
        } else {
            throw new RuntimeException("Document not found with id " + documentId);
        }
    }

    public Optional<AnalysisResult> findAnalysisResultById(Long id) {
        return analysisResultRepository.findById(id);
    }

    public List<AnalysisResult> findAnalysisResultsByDocumentId(Long documentId) {
        return analysisResultRepository.findByDocument_DocumentId(documentId);
    }

    public void deleteAnalysisResultById(Long id) {
        analysisResultRepository.deleteById(id);
    }
}
