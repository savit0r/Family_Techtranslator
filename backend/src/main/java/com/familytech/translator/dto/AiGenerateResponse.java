package com.familytech.translator.dto;

public record AiGenerateResponse(
    String text,
    String model,
    String provider,
    Long generationTimeMs,
    boolean mock
) {}
