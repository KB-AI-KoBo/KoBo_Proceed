package com.kb.kobo.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UpdatedUserDto {
    private String username;
    private String password;
    private String email;
    private String companyName;
    private String companySize;
    private String registrationNumber;
    private String companyEmail;
    private String industry;
}
