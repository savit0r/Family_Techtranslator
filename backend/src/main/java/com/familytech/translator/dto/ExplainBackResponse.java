package com.familytech.translator.dto;

public record ExplainBackResponse(
    Long translationId,
    String concept,
    String userExplanation,
    String whatTheyUnderstood,
    String misunderstandings,
    String shortClarification,
    String fullEvaluationText,
    String modelUsed,
    String providerUsed,
    Long generationTimeMs
) {}
