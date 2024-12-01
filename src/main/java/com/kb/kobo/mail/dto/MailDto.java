package com.kb.kobo.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private String from;
    private String to;
    private String title;
    private String message;
}
