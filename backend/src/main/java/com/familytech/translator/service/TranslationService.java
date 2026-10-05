package com.familytech.translator.service;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.dto.TranslationRequest;
import com.familytech.translator.dto.TranslationResponse;
import com.familytech.translator.dto.ExplainBackRequest;
import com.familytech.translator.dto.ExplainBackResponse;
import com.familytech.translator.exception.ResourceNotFoundException;
import com.familytech.translator.model.FamilyMember;
import com.familytech.translator.model.TranslationHistory;
import com.familytech.translator.repository.TranslationHistoryRepository;
import com.familytech.translator.service.ai.AiGenerationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TranslationService {

    private final FamilyMemberService familyMemberService;
    private final PromptTemplateService promptTemplateService;
    private final AiGenerationService aiGenerationService;
    private final TranslationParser translationParser;
    private final ExplainBackParser explainBackParser;
    private final TranslationHistoryRepository historyRepository;

    public TranslationService(
            FamilyMemberService familyMemberService,
            PromptTemplateService promptTemplateService,
            AiGenerationService aiGenerationService,
            TranslationParser translationParser,
            ExplainBackParser explainBackParser,
            TranslationHistoryRepository historyRepository) {
        this.familyMemberService = familyMemberService;
        this.promptTemplateService = promptTemplateService;
        this.aiGenerationService = aiGenerationService;
        this.translationParser = translationParser;
        this.explainBackParser = explainBackParser;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public TranslationResponse translateConcept(TranslationRequest request) {
        FamilyMember member = familyMemberService.findEntityById(request.familyMemberId());
        String compiledPrompt = promptTemplateService.buildPrompt(member, request.concept());

        AiGenerateRequest aiRequest = new AiGenerateRequest(
            compiledPrompt,
            "You are an expert technical communicator explaining concepts to real family members.",
            0.7,
            800
        );

        AiGenerateResponse aiResponse = aiGenerationService.generate(aiRequest);

        TranslationParser.ParsedExplanation parsed = translationParser.parseAndValidate(
            aiResponse.text(), request.concept()
        );

        TranslationHistory history = new TranslationHistory(
            member,
            request.concept().trim(),
            promptTemplateService.getPromptVersion(),
            aiResponse.model(),
            aiResponse.provider(),
            parsed.analogySummary(),
            parsed.conceptBreakdown(),
            parsed.analogyToTechMapping(),
            parsed.keyTakeaway(),
            parsed.followUpQuestion(),
            parsed.fullExplanation(),
            aiResponse.generationTimeMs()
        );

        TranslationHistory saved = historyRepository.save(history);
        return TranslationResponse.fromEntity(saved);
    }

    @Transactional
    public TranslationResponse recordFeedback(Long translationId, String feedback) {
        TranslationHistory history = historyRepository.findById(translationId)
                .orElseThrow(() -> new ResourceNotFoundException("Translation not found with id: " + translationId));
        history.setFeedback(feedback);
        TranslationHistory saved = historyRepository.save(history);
        return TranslationResponse.fromEntity(saved);
    }

    public ExplainBackResponse evaluateExplainBack(ExplainBackRequest request) {
        TranslationHistory history = historyRepository.findById(request.translationId())
                .orElseThrow(() -> new ResourceNotFoundException("Translation not found with id: " + request.translationId()));

        String evalPrompt = promptTemplateService.buildExplainBackEvalPrompt(
            history.getConcept(),
            history.getFullExplanation(),
            request.userExplanation()
        );

        AiGenerateRequest aiRequest = new AiGenerateRequest(
            evalPrompt,
            "You are an encouraging educational AI tutor evaluating technical understanding.",
            0.5,
            600
        );

        AiGenerateResponse aiResponse = aiGenerationService.generate(aiRequest);

        ExplainBackParser.ParsedExplainBack parsed = explainBackParser.parseAndValidate(
            aiResponse.text(), history.getConcept()
        );

        return new ExplainBackResponse(
            history.getId(),
            history.getConcept(),
            request.userExplanation(),
            parsed.whatTheyUnderstood(),
            parsed.misunderstandings(),
            parsed.shortClarification(),
            parsed.fullEvaluationText(),
            aiResponse.model(),
            aiResponse.provider(),
            aiResponse.generationTimeMs()
        );
    }

    public List<TranslationResponse> getHistoryForFamilyMember(Long familyMemberId) {
        // Ensure family member exists
        familyMemberService.findEntityById(familyMemberId);
        return historyRepository.findByFamilyMemberIdOrderByCreatedAtDesc(familyMemberId)
                .stream()
                .map(TranslationResponse::fromEntity)
                .toList();
    }
}
