package com.familytech.translator.dto;

import com.familytech.translator.model.TechLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FamilyMemberRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @NotBlank(message = "Relationship is required")
    @Size(max = 50, message = "Relationship must not exceed 50 characters")
    String relationship,

    @NotBlank(message = "Occupation is required")
    @Size(max = 100, message = "Occupation must not exceed 100 characters")
    String occupation,

    @NotBlank(message = "Interests/Hobbies are required")
    String interests,

    @NotBlank(message = "Familiar topics are required")
    String familiarTopics,

    @NotNull(message = "Technical level is required")
    TechLevel techLevel,

    @NotBlank(message = "Preferred language is required")
    @Size(max = 50, message = "Preferred language must not exceed 50 characters")
    String preferredLanguage,

    @NotBlank(message = "Communication style is required")
    @Size(max = 100, message = "Communication style must not exceed 100 characters")
    String communicationStyle
) {}
