package com.familytech.translator.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "translation_history")
public class TranslationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_member_id", nullable = false)
    private FamilyMember familyMember;

    @Column(nullable = false, length = 200)
    private String concept;

    @Column(name = "prompt_version", nullable = false, length = 20)
    private String promptVersion;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "provider_name", nullable = false, length = 50)
    private String providerName;

    @Column(name = "analogy_summary", nullable = false, columnDefinition = "TEXT")
    private String analogySummary;

    @Column(name = "concept_breakdown", nullable = false, columnDefinition = "TEXT")
    private String conceptBreakdown;

    @Column(name = "analogy_to_tech_mapping", nullable = false, columnDefinition = "TEXT")
    private String analogyToTechMapping;

    @Column(name = "key_takeaway", nullable = false, columnDefinition = "TEXT")
    private String keyTakeaway;

    @Column(name = "follow_up_question", columnDefinition = "TEXT")
    private String followUpQuestion;

    @Column(name = "feedback", length = 20)
    private String feedback;

    @Column(name = "full_explanation", nullable = false, columnDefinition = "TEXT")
    private String fullExplanation;

    @Column(name = "generation_time_ms")
    private Long generationTimeMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    public TranslationHistory() {
    }

    public TranslationHistory(FamilyMember familyMember, String concept, String promptVersion, String modelName, String providerName,
                              String analogySummary, String conceptBreakdown, String analogyToTechMapping, String keyTakeaway,
                              String followUpQuestion, String fullExplanation, Long generationTimeMs) {
        this.familyMember = familyMember;
        this.concept = concept;
        this.promptVersion = promptVersion;
        this.modelName = modelName;
        this.providerName = providerName;
        this.analogySummary = analogySummary;
        this.conceptBreakdown = conceptBreakdown;
        this.analogyToTechMapping = analogyToTechMapping;
        this.keyTakeaway = keyTakeaway;
        this.followUpQuestion = followUpQuestion;
        this.fullExplanation = fullExplanation;
        this.generationTimeMs = generationTimeMs;
    }

    public TranslationHistory(FamilyMember familyMember, String concept, String promptVersion, String modelName, String providerName,
                              String analogySummary, String conceptBreakdown, String analogyToTechMapping, String keyTakeaway,
                              String fullExplanation, Long generationTimeMs) {
        this(familyMember, concept, promptVersion, modelName, providerName, analogySummary, conceptBreakdown, analogyToTechMapping, keyTakeaway, null, fullExplanation, generationTimeMs);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FamilyMember getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(FamilyMember familyMember) {
        this.familyMember = familyMember;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getPromptVersion() {
        return promptVersion;
    }

    public void setPromptVersion(String promptVersion) {
        this.promptVersion = promptVersion;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getAnalogySummary() {
        return analogySummary;
    }

    public void setAnalogySummary(String analogySummary) {
        this.analogySummary = analogySummary;
    }

    public String getConceptBreakdown() {
        return conceptBreakdown;
    }

    public void setConceptBreakdown(String conceptBreakdown) {
        this.conceptBreakdown = conceptBreakdown;
    }

    public String getAnalogyToTechMapping() {
        return analogyToTechMapping;
    }

    public void setAnalogyToTechMapping(String analogyToTechMapping) {
        this.analogyToTechMapping = analogyToTechMapping;
    }

    public String getKeyTakeaway() {
        return keyTakeaway;
    }

    public void setKeyTakeaway(String keyTakeaway) {
        this.keyTakeaway = keyTakeaway;
    }

    public String getFollowUpQuestion() {
        return followUpQuestion;
    }

    public void setFollowUpQuestion(String followUpQuestion) {
        this.followUpQuestion = followUpQuestion;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFullExplanation() {
        return fullExplanation;
    }

    public void setFullExplanation(String fullExplanation) {
        this.fullExplanation = fullExplanation;
    }

    public Long getGenerationTimeMs() {
        return generationTimeMs;
    }

    public void setGenerationTimeMs(Long generationTimeMs) {
        this.generationTimeMs = generationTimeMs;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
