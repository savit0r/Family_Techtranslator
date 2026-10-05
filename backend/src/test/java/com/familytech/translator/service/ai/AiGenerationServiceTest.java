package com.familytech.translator.service.ai;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.exception.LlmProviderUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiGenerationServiceTest {

    @Mock
    private OllamaLlmProvider ollamaLlmProvider;

    private MockLlmProvider mockLlmProvider;
    private AiGenerateRequest sampleRequest;

    @BeforeEach
    void setUp() {
        mockLlmProvider = new MockLlmProvider();
        sampleRequest = new AiGenerateRequest("Test prompt", null, 0.7, 100);
        lenient().when(ollamaLlmProvider.getProviderName()).thenReturn("ollama");
    }

    @Test
    void generate_whenOllamaAvailable_usesOllama() {
        AiGenerationService service = new AiGenerationService(ollamaLlmProvider, mockLlmProvider, "ollama", true);

        when(ollamaLlmProvider.isAvailable()).thenReturn(true);
        when(ollamaLlmProvider.generate(any())).thenReturn(
                new AiGenerateResponse("Ollama generated text", "llama3.2:3b", "ollama", 150L, false)
        );

        AiGenerateResponse response = service.generate(sampleRequest);

        assertThat(response.provider()).isEqualTo("ollama");
        assertThat(response.mock()).isFalse();
        assertThat(response.text()).isEqualTo("Ollama generated text");
    }

    @Test
    void generate_whenOllamaUnavailableAndMockFallbackAllowed_usesMock() {
        AiGenerationService service = new AiGenerationService(ollamaLlmProvider, mockLlmProvider, "ollama", true);

        when(ollamaLlmProvider.isAvailable()).thenReturn(false);

        AiGenerateResponse response = service.generate(sampleRequest);

        assertThat(response.provider()).isEqualTo("mock");
        assertThat(response.mock()).isTrue();
    }

    @Test
    void generate_whenOllamaUnavailableAndMockFallbackDisabled_throwsException() {
        AiGenerationService service = new AiGenerationService(ollamaLlmProvider, mockLlmProvider, "ollama", false);

        when(ollamaLlmProvider.isAvailable()).thenReturn(false);
        when(ollamaLlmProvider.generate(any())).thenThrow(new LlmProviderUnavailableException("Ollama offline"));

        assertThatThrownBy(() -> service.generate(sampleRequest))
                .isInstanceOf(LlmProviderUnavailableException.class)
                .hasMessageContaining("Ollama offline");
    }

    @Test
    void getStatus_returnsProviderHealth() {
        AiGenerationService service = new AiGenerationService(ollamaLlmProvider, mockLlmProvider, "ollama", true);
        when(ollamaLlmProvider.isAvailable()).thenReturn(true);

        Map<String, Object> status = service.getStatus();

        assertThat(status.get("configuredProvider")).isEqualTo("ollama");
        assertThat(status.get("ollamaAvailable")).isEqualTo(true);
        assertThat(status.get("activeProvider")).isEqualTo("ollama");
    }
}
