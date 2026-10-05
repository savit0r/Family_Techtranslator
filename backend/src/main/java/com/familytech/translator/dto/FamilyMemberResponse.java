package com.familytech.translator.dto;

import com.familytech.translator.model.FamilyMember;
import com.familytech.translator.model.TechLevel;
import java.time.ZonedDateTime;

public record FamilyMemberResponse(
    Long id,
    String name,
    String relationship,
    String occupation,
    String interests,
    String familiarTopics,
    TechLevel techLevel,
    String preferredLanguage,
    String communicationStyle,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {
    public static FamilyMemberResponse fromEntity(FamilyMember entity) {
        return new FamilyMemberResponse(
            entity.getId(),
            entity.getName(),
            entity.getRelationship(),
            entity.getOccupation(),
            entity.getInterests(),
            entity.getFamiliarTopics(),
            entity.getTechLevel(),
            entity.getPreferredLanguage(),
            entity.getCommunicationStyle(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
