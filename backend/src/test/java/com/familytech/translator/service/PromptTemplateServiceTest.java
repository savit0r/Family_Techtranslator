package com.familytech.translator.service;

import com.familytech.translator.model.FamilyMember;
import com.familytech.translator.model.TechLevel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PromptTemplateServiceTest {

    private final PromptTemplateService promptTemplateService = new PromptTemplateService();

    @Test
    void buildPrompt_replacesAllPlaceholdersWithProfileContext() {
        FamilyMember member = new FamilyMember(
            "Maria",
            "Grandmother",
            "Gardener",
            "Planting roses, baking bread",
            "Soil nutrients, pruning, seasonal flower cycles",
            TechLevel.BEGINNER,
            "Spanish",
            "Storyteller & Visual"
        );

        String prompt = promptTemplateService.buildPrompt(member, "Database Index");

        assertThat(prompt).contains("Maria");
        assertThat(prompt).contains("Grandmother");
        assertThat(prompt).contains("Gardener");
        assertThat(prompt).contains("Planting roses, baking bread");
        assertThat(prompt).contains("Soil nutrients, pruning, seasonal flower cycles");
        assertThat(prompt).contains("BEGINNER");
        assertThat(prompt).contains("Spanish");
        assertThat(prompt).contains("Storyteller & Visual");
        assertThat(prompt).contains("Database Index");
        assertThat(prompt).contains("## Analogy Summary");
        assertThat(prompt).contains("## Analogy-to-Tech Mapping");
    }
}
