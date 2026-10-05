package com.familytech.translator.service;

import com.familytech.translator.model.FamilyMember;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class PromptTemplateService {

    private static final String PROMPT_VERSION = "v1.0";
    private final String templateContent;
    private final String evalTemplateContent;

    public PromptTemplateService() {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/explanation-v1.st");
            this.templateContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            ClassPathResource evalResource = new ClassPathResource("prompts/eval-explain-back-v1.st");
            this.evalTemplateContent = StreamUtils.copyToString(evalResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load prompt template files", e);
        }
    }

    public String buildPrompt(FamilyMember member, String concept) {
        return templateContent
                .replace("{{name}}", safeValue(member.getName()))
                .replace("{{relationship}}", safeValue(member.getRelationship()))
                .replace("{{occupation}}", safeValue(member.getOccupation()))
                .replace("{{interests}}", safeValue(member.getInterests()))
                .replace("{{familiarTopics}}", safeValue(member.getFamiliarTopics()))
                .replace("{{techLevel}}", member.getTechLevel() != null ? member.getTechLevel().name() : "BEGINNER")
                .replace("{{preferredLanguage}}", safeValue(member.getPreferredLanguage()))
                .replace("{{communicationStyle}}", safeValue(member.getCommunicationStyle()))
                .replace("{{concept}}", safeValue(concept));
    }

    public String buildExplainBackEvalPrompt(String concept, String originalExplanation, String userExplanation) {
        return evalTemplateContent
                .replace("{{concept}}", safeValue(concept))
                .replace("{{originalExplanation}}", safeValue(originalExplanation))
                .replace("{{userExplanation}}", safeValue(userExplanation));
    }

    public String getPromptVersion() {
        return PROMPT_VERSION;
    }

    private String safeValue(String val) {
        return val != null ? val.trim() : "";
    }
}
