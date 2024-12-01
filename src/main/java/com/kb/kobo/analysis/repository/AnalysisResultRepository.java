package com.kb.kobo.analysis.repository;

import com.kb.kobo.analysis.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
    List<AnalysisResult> findByDocument_DocumentId(Long documentId);
}
