package com.kb.kobo.analysis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private Integer conversationId;
    private SenderType senderType;
    private String content;

    public enum SenderType {
        USER,
        ADMIN
    }
}
