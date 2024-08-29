package com.kb.kobo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @JoinColumn(name = "document_id", nullable = true) // 문서는 선택적
    private String documentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 사용자 ID 기준
    private User user;

    @Column(name = "content", nullable = false)
    private String content;
//
//    @Column(name = "response", columnDefinition = "TEXT")
//    private String response;
}
