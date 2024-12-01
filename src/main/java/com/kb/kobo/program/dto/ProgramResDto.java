package com.kb.kobo.program.dto;

import com.kb.kobo.program.domain.SupportProgram;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProgramResDto {
    private List<SupportProgram> data;
}

