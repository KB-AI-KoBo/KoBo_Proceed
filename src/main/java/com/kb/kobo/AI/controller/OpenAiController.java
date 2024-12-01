package com.kb.kobo.AI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kb.kobo.AI.service.OpenAiService;
import com.kb.kobo.program.domain.SupportProgram;
import java.util.List;


@RestController
@RequestMapping("/api")
public class OpenAiController {

    private final OpenAiService openAiService;

    @Autowired
    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/programs")
    public ResponseEntity<List<SupportProgram>> getPrograms() {
        String input = "Provide data for supportPrograms.";
        List<SupportProgram> supportPrograms = openAiService.callOpenAiApi(input);
        return ResponseEntity.ok(supportPrograms);
    }
}
