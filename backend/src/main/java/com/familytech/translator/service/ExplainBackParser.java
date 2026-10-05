package com.familytech.translator.service;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExplainBackParser {

    public record ParsedExplainBack(
        String whatTheyUnderstood,
        String misunderstandings,
        String shortClarification,
        String fullEvaluationText
    ) {}

    public ParsedExplainBack parseAndValidate(String rawOutput, String concept) {
        if (rawOutput == null || rawOutput.isBlank()) {
            return fallbackParsed("The AI model returned an empty evaluation.", concept);
        }

        String cleaned = rawOutput.trim();

        String understood = extractSection(cleaned, "What They Understood");
        String gaps = extractSection(cleaned, "Misunderstandings & Gaps");
        if (gaps.isBlank()) {
            gaps = extractSection(cleaned, "Misunderstandings");
        }
        String clarification = extractSection(cleaned, "Short Clarification");
        if (clarification.isBlank()) {
            clarification = extractSection(cleaned, "Clarification");
        }

        if (understood.isBlank() && gaps.isBlank() && clarification.isBlank()) {
            return fallbackParsed(cleaned, concept);
        }

        if (understood.isBlank()) {
            understood = "- Demonstrated solid interest and restated core aspects of " + concept + ".";
        }
        if (gaps.isBlank()) {
            gaps = "No major misconceptions detected.";
        }
        if (clarification.isBlank()) {
            clarification = "Great effort! Continuous practice helps solidify intuitive technical knowledge.";
        }

        return new ParsedExplainBack(understood.trim(), gaps.trim(), clarification.trim(), cleaned);
    }

    private String extractSection(String content, String sectionTitle) {
        Pattern pattern = Pattern.compile("(?i)##\\s*" + Pattern.quote(sectionTitle) + "\\s*\\n+(.*?)(?=\\n*##|\\z)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private ParsedExplainBack fallbackParsed(String rawText, String concept) {
        return new ParsedExplainBack(
            "- Demonstrated effort in restating " + concept + ".",
            "No major misconceptions detected.",
            rawText.isBlank() ? "Great work practicing explaining " + concept + "!" : rawText,
            rawText
        );
    }
}
