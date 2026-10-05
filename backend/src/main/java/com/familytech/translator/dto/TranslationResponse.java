package com.familytech.translator.dto;

import com.familytech.translator.model.TranslationHistory;
import java.time.ZonedDateTime;

public record TranslationResponse(
    Long id,
    Long familyMemberId,
    String familyMemberName,
    String concept,
    String analogySummary,
    String conceptBreakdown,
    String analogyToTechMapping,
    String keyTakeaway,
    String followUpQuestion,
    String feedback,
    String fullExplanation,
    String modelUsed,
    String providerUsed,
    Long generationTimeMs,
    ZonedDateTime createdAt
) {
    public static TranslationResponse fromEntity(TranslationHistory history) {
        return new TranslationResponse(
            history.getId(),
            history.getFamilyMember().getId(),
            history.getFamilyMember().getName(),
            history.getConcept(),
            history.getAnalogySummary(),
            history.getConceptBreakdown(),
            history.getAnalogyToTechMapping(),
            history.getKeyTakeaway(),
            history.getFollowUpQuestion(),
            history.getFeedback(),
            history.getFullExplanation(),
            history.getModelName(),
            history.getProviderName(),
            history.getGenerationTimeMs(),
            history.getCreatedAt()
        );
    }
}
