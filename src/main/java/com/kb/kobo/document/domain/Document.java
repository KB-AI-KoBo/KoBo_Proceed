package com.kb.kobo.document.domain;

import com.kb.kobo.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String documentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType documentType;

    @Column(nullable = false, length = 255)
    private String documentPath;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp uploadedAt;

    public enum FileType {
        PDF
    }
}
