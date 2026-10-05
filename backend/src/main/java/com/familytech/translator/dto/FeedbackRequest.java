package com.familytech.translator.dto;

import jakarta.validation.constraints.NotBlank;

public record FeedbackRequest(
    @NotBlank(message = "Feedback is required")
    String feedback,
    String comment
) {}
