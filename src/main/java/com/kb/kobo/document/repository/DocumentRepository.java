package com.kb.kobo.document.repository;

import com.kb.kobo.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentId(Long documentId);
    List<Document> findByUserId(Long userId);
}
