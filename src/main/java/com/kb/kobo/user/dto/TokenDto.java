package com.kb.kobo.user.dto;

import lombok.*;
import com.google.gson.annotations.SerializedName;

@Builder
@AllArgsConstructor
@Data
public class TokenDto {
    @SerializedName("access_token")
    String access_token;
}
