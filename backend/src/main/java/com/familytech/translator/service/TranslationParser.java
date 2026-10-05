package com.familytech.translator.service;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TranslationParser {

    public record ParsedExplanation(
        String analogySummary,
        String conceptBreakdown,
        String analogyToTechMapping,
        String keyTakeaway,
        String followUpQuestion,
        String fullExplanation
    ) {}

    public ParsedExplanation parseAndValidate(String rawOutput, String concept) {
        if (rawOutput == null || rawOutput.isBlank()) {
            return fallbackParsed("The AI model returned an empty explanation.", concept);
        }

        String cleaned = rawOutput.trim();

        String summary = extractSection(cleaned, "Analogy Summary");
        String breakdown = extractSection(cleaned, "Concept Breakdown");
        if (breakdown.isBlank()) {
            breakdown = extractSection(cleaned, "How it Works");
        }
        String mapping = extractSection(cleaned, "Analogy-to-Tech Mapping");
        String takeaway = extractSection(cleaned, "Key Takeaway");
        String followUp = extractSection(cleaned, "Follow-up Question");
        if (followUp.isBlank()) {
            followUp = extractSection(cleaned, "Suggested Follow-up Question");
        }

        // Fallback for malformed AI output missing standard markdown headers
        if (summary.isBlank() && breakdown.isBlank() && mapping.isBlank() && takeaway.isBlank()) {
            return fallbackParsed(cleaned, concept);
        }

        if (summary.isBlank()) {
            summary = "Summary of " + concept + " analogy.";
        }
        if (breakdown.isBlank()) {
            breakdown = cleaned;
        }
        if (mapping.isBlank()) {
            mapping = "- Core Concept: " + concept;
        }
        if (takeaway.isBlank()) {
            takeaway = "Understanding " + concept + " through relatable real-world concepts.";
        }
        if (followUp.isBlank()) {
            followUp = "How does " + concept + " adapt when real-world scale or complexity increases?";
        }

        return new ParsedExplanation(summary.trim(), breakdown.trim(), mapping.trim(), takeaway.trim(), followUp.trim(), cleaned);
    }

    private String extractSection(String content, String sectionTitle) {
        // Pattern matches ## Section Title until next ## or end of string
        Pattern pattern = Pattern.compile("(?i)##\\s*" + Pattern.quote(sectionTitle) + "\\s*\\n+(.*?)(?=\\n*##|\\z)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private ParsedExplanation fallbackParsed(String rawText, String concept) {
        return new ParsedExplanation(
            "Overview of " + concept,
            rawText,
            "- Concept: " + concept,
            "Key takeaway for " + concept,
            "How does " + concept + " apply in your daily context?",
            rawText
        );
    }
}
