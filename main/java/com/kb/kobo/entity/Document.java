package com.kb.kobo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "email", nullable = true)
    private User email;

    @Column(name = "file_name", nullable = true, length = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = true)
    private FileType fileType;

    @Column(name = "file_path", nullable = true, length = 255)
    private String filePath;

    @Column(name = "uploaded_at", nullable = true, updatable = false)
    @CreationTimestamp
    private Timestamp uploadedAt;



    public enum FileType {
        PDF
    }
}
