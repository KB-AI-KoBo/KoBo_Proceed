package com.kb.kobo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "analysis_results")
@Getter
@Setter
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Document document;

    @Column(name = "question_content", nullable = false)
    private String questionContent;

    @Column(name = "result", columnDefinition = "TEXT")
    private String result;


}
