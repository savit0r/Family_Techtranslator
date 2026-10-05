package com.familytech.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AiGenerateRequest(
    @NotBlank(message = "Prompt is required")
    String prompt,

    String systemPrompt,

    Double temperature,

    @Positive(message = "Max tokens must be positive")
    Integer maxTokens
) {}
