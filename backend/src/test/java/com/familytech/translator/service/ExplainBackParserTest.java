package com.familytech.translator.service;

import com.familytech.translator.service.ExplainBackParser.ParsedExplainBack;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExplainBackParserTest {

    private final ExplainBackParser parser = new ExplainBackParser();

    @Test
    void parseAndValidate_structuredMarkdown_extractsAllSections() {
        String input = """
                ## What They Understood
                - Correctly explained that a database index works like a cookbook index.
                - Understood that it speeds up search operations.

                ## Misunderstandings & Gaps
                - Omitted that indexes consume additional memory/storage space.

                ## Short Clarification
                Awesome job! Remember that while indexes make searching much faster, they use a bit of extra space to store the key locations.
                """;

        ParsedExplainBack result = parser.parseAndValidate(input, "Database Index");

        assertThat(result.whatTheyUnderstood()).contains("cookbook index");
        assertThat(result.misunderstandings()).contains("additional memory/storage space");
        assertThat(result.shortClarification()).contains("Awesome job");
    }

    @Test
    void parseAndValidate_malformedOutput_usesFallbackParsing() {
        String malformedInput = "They understood the basic metaphor well.";

        ParsedExplainBack result = parser.parseAndValidate(malformedInput, "Kubernetes");

        assertThat(result.whatTheyUnderstood()).contains("Kubernetes");
        assertThat(result.misunderstandings()).contains("No major misconceptions detected.");
        assertThat(result.shortClarification()).isEqualTo(malformedInput);
    }
}
