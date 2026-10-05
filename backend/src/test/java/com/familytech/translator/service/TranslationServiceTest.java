package com.familytech.translator.service;

import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.dto.TranslationRequest;
import com.familytech.translator.dto.TranslationResponse;
import com.familytech.translator.model.FamilyMember;
import com.familytech.translator.model.TechLevel;
import com.familytech.translator.model.TranslationHistory;
import com.familytech.translator.repository.TranslationHistoryRepository;
import com.familytech.translator.service.ai.AiGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private FamilyMemberService familyMemberService;

    @Mock
    private PromptTemplateService promptTemplateService;

    @Mock
    private AiGenerationService aiGenerationService;

    @Spy
    private TranslationParser translationParser = new TranslationParser();

    @Spy
    private ExplainBackParser explainBackParser = new ExplainBackParser();

    @Mock
    private TranslationHistoryRepository historyRepository;

    @InjectMocks
    private TranslationService translationService;

    private FamilyMember sampleMember;

    @BeforeEach
    void setUp() {
        sampleMember = new FamilyMember(
            "Maria", "Grandmother", "Gardener", "Planting roses", "Soil nutrients", TechLevel.BEGINNER, "Spanish", "Storyteller"
        );
        sampleMember.setId(1L);
    }

    @Test
    void translateConcept_validRequest_generatesParsesAndSaves() {
        TranslationRequest request = new TranslationRequest(1L, "Kubernetes");
        String mockAiText = """
                ## Analogy Summary
                Kubernetes is like a master greenhouse manager orchestrating automatic plant pots.

                ## Concept Breakdown
                1. Individual pots hold plants (Containers).
                2. Automated systems water and adjust light automatically (Pods & Nodes).

                ## Analogy-to-Tech Mapping
                - Pot -> Container
                - Greenhouse Manager -> Kubernetes Cluster

                ## Key Takeaway
                Kubernetes manages applications automatically so they never wilt.
                """;

        when(familyMemberService.findEntityById(1L)).thenReturn(sampleMember);
        when(promptTemplateService.buildPrompt(any(), any())).thenReturn("Compiled prompt template");
        when(promptTemplateService.getPromptVersion()).thenReturn("v1.0");
        when(aiGenerationService.generate(any())).thenReturn(
            new AiGenerateResponse(mockAiText, "llama3.2:3b", "ollama", 350L, false)
        );

        TranslationHistory savedHistory = new TranslationHistory(
            sampleMember, "Kubernetes", "v1.0", "llama3.2:3b", "ollama",
            "Kubernetes is like a master greenhouse manager", "Breakdown", "Mapping", "Key Takeaway", mockAiText, 350L
        );
        savedHistory.setId(10L);

        when(historyRepository.save(any(TranslationHistory.class))).thenReturn(savedHistory);

        TranslationResponse response = translationService.translateConcept(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.familyMemberName()).isEqualTo("Maria");
        assertThat(response.concept()).isEqualTo("Kubernetes");
        assertThat(response.modelUsed()).isEqualTo("llama3.2:3b");
        verify(historyRepository, times(1)).save(any(TranslationHistory.class));
    }

    @Test
    void evaluateExplainBack_validInput_returnsParsedEvaluation() {
        TranslationHistory history = new TranslationHistory(
            sampleMember, "Database Index", "v1.0", "llama3.2:3b", "ollama",
            "Summary", "Breakdown", "Mapping", "Takeaway", "Full explanation", 200L
        );
        history.setId(5L);

        when(historyRepository.findById(5L)).thenReturn(java.util.Optional.of(history));
        when(promptTemplateService.buildExplainBackEvalPrompt(any(), any(), any())).thenReturn("Compiled eval prompt");

        String evalAiOutput = """
                ## What They Understood
                - Understood that index speeds up finding items.

                ## Misunderstandings & Gaps
                - No major misconceptions detected.

                ## Short Clarification
                Great job restating the concept!
                """;

        when(aiGenerationService.generate(any())).thenReturn(
            new AiGenerateResponse(evalAiOutput, "llama3.2:3b", "ollama", 180L, false)
        );

        com.familytech.translator.dto.ExplainBackRequest req = new com.familytech.translator.dto.ExplainBackRequest(5L, "An index helps you find items without looking everywhere.");
        com.familytech.translator.dto.ExplainBackResponse response = translationService.evaluateExplainBack(req);

        assertThat(response.translationId()).isEqualTo(5L);
        assertThat(response.concept()).isEqualTo("Database Index");
        assertThat(response.whatTheyUnderstood()).contains("speeds up finding items");
        assertThat(response.shortClarification()).contains("Great job");
    }

    @Test
    void getHistoryForFamilyMember_returnsHistoryList() {
        when(familyMemberService.findEntityById(1L)).thenReturn(sampleMember);

        TranslationHistory history = new TranslationHistory(
            sampleMember, "API", "v1.0", "llama3.2:3b", "ollama",
            "Summary", "Breakdown", "Mapping", "Takeaway", "Full explanation", 200L
        );
        history.setId(5L);

        when(historyRepository.findByFamilyMemberIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(history));

        List<TranslationResponse> result = translationService.getHistoryForFamilyMember(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().concept()).isEqualTo("API");
    }
}
