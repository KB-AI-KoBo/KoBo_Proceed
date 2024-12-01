package com.kb.kobo.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSignupReqDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String companyName;
    private String companySize;
    private String registrationNumber;
    private String companyEmail;
    private String industry;
}
