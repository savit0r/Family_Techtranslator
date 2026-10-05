package com.familytech.translator.service.ai;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MockLlmProviderTest {

    private final MockLlmProvider mockLlmProvider = new MockLlmProvider();

    @Test
    void generate_returnsMockResponse() {
        AiGenerateRequest request = new AiGenerateRequest("Explain database index", null, 0.7, 500);

        AiGenerateResponse response = mockLlmProvider.generate(request);

        assertThat(response).isNotNull();
        assertThat(response.provider()).isEqualTo("mock");
        assertThat(response.mock()).isTrue();
        assertThat(response.text()).contains("Analogy Summary");
    }

    @Test
    void isAvailable_returnsTrue() {
        assertThat(mockLlmProvider.isAvailable()).isTrue();
    }
}
