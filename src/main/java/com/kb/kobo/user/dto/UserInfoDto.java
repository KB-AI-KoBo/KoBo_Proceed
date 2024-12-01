package com.kb.kobo.user.dto;

import com.kb.kobo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String email;
    private String companyName;
    private String companySize;
    private String registrationNumber;
    private String companyEmail;
    private String industry;

    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .companyName(user.getCompanyName())
                .companySize(user.getCompanySize())
                .registrationNumber(user.getRegistrationNumber())
                .companyEmail(user.getCompanyEmail())
                .build();
    }
}
