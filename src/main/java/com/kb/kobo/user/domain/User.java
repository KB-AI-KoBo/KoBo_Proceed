package com.kb.kobo.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "사용자 이름은 필수입니다.")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6글자 이상이어야 합니다.")
    private String password;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "회사 이름은 필수입니다.")
    private String companyName;

    @Column(nullable = false)
    private String companySize;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "사업자 등록 번호는 필수입니다.")
    private String registrationNumber;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "회사 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String companyEmail;

    @Column(nullable = false)
    @NotBlank(message = "산업 분야는 필수입니다.")
    private String industry;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Builder
    public User(Long id, String username, String password, String email, String companyName, String companySize, String registrationNumber, String companyEmail, String industry, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.companyName = companyName;
        this.companySize = companySize;
        this.registrationNumber = registrationNumber;
        this.companyEmail = companyEmail;
        this.industry = industry;
        this.createdAt = LocalDateTime.now();
    }
}
