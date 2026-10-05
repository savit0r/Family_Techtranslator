package com.familytech.translator.service;

import com.familytech.translator.service.TranslationParser.ParsedExplanation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TranslationParserTest {

    private final TranslationParser parser = new TranslationParser();

    @Test
    void parseAndValidate_structuredMarkdown_extractsAllSections() {
        String input = """
                ## Analogy Summary
                A database index is like a plant catalog at a garden nursery.

                ## Concept Breakdown
                1. Nursery beds hold thousands of plants without labels.
                2. The catalog indexes plants alphabetically by species, listing row numbers.

                ## Analogy-to-Tech Mapping
                - Nursery Row Number -> Database Record Pointer
                - Plant Species Name -> Indexed Column Key

                ## Key Takeaway
                Indexes save time by telling you where to look instantly.
                """;

        ParsedExplanation result = parser.parseAndValidate(input, "Database Index");

        assertThat(result.analogySummary()).isEqualTo("A database index is like a plant catalog at a garden nursery.");
        assertThat(result.conceptBreakdown()).contains("Nursery beds hold thousands");
        assertThat(result.analogyToTechMapping()).contains("Nursery Row Number -> Database Record Pointer");
        assertThat(result.keyTakeaway()).isEqualTo("Indexes save time by telling you where to look instantly.");
    }

    @Test
    void parseAndValidate_malformedAiOutputWithoutHeaders_usesFallbackParsing() {
        String malformedInput = "Imagine you have a giant garden and you use signs to quickly find tomato beds.";

        ParsedExplanation result = parser.parseAndValidate(malformedInput, "Database Index");

        assertThat(result.analogySummary()).contains("Overview of Database Index");
        assertThat(result.conceptBreakdown()).isEqualTo(malformedInput);
        assertThat(result.analogyToTechMapping()).contains("Database Index");
        assertThat(result.keyTakeaway()).contains("Database Index");
    }
}
