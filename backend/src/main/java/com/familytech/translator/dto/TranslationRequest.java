package com.familytech.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TranslationRequest(
    @NotNull(message = "Family member ID is required")
    Long familyMemberId,

    @NotBlank(message = "Technical concept is required")
    @Size(max = 200, message = "Technical concept must not exceed 200 characters")
    String concept
) {}
