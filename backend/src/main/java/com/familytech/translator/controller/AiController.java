package com.familytech.translator.controller;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.service.ai.AiGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiGenerationService aiGenerationService;

    public AiController(AiGenerationService aiGenerationService) {
        this.aiGenerationService = aiGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<AiGenerateResponse> generate(@Valid @RequestBody AiGenerateRequest request) {
        AiGenerateResponse response = aiGenerationService.generate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(aiGenerationService.getStatus());
    }
}
