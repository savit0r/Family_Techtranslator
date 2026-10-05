package com.familytech.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExplainBackRequest(
    @NotNull(message = "Translation ID is required")
    Long translationId,

    @NotBlank(message = "User explanation is required")
    String userExplanation
) {}
