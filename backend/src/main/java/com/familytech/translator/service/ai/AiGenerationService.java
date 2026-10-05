package com.familytech.translator.service.ai;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.exception.LlmProviderUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiGenerationService {

    private static final Logger log = LoggerFactory.getLogger(AiGenerationService.class);

    private final OllamaLlmProvider ollamaLlmProvider;
    private final MockLlmProvider mockLlmProvider;
    private final String configuredProvider;
    private final boolean allowMockFallback;

    public AiGenerationService(
            OllamaLlmProvider ollamaLlmProvider,
            MockLlmProvider mockLlmProvider,
            @Value("${app.llm.provider:ollama}") String configuredProvider,
            @Value("${app.llm.allow-mock-fallback:true}") boolean allowMockFallback) {
        this.ollamaLlmProvider = ollamaLlmProvider;
        this.mockLlmProvider = mockLlmProvider;
        this.configuredProvider = configuredProvider;
        this.allowMockFallback = allowMockFallback;
    }

    public AiGenerateResponse generate(AiGenerateRequest request) {
        LlmProvider activeProvider = selectProvider();
        try {
            log.info("Generating text using provider: {}", activeProvider.getProviderName());
            return activeProvider.generate(request);
        } catch (LlmProviderUnavailableException ex) {
            if (allowMockFallback && !(activeProvider instanceof MockLlmProvider)) {
                log.warn("Configured provider {} unavailable. Falling back to MockLlmProvider. Cause: {}",
                        activeProvider.getProviderName(), ex.getMessage());
                return mockLlmProvider.generate(request);
            }
            throw ex;
        }
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("configuredProvider", configuredProvider);
        status.put("allowMockFallback", allowMockFallback);

        boolean ollamaAvailable = ollamaLlmProvider.isAvailable();
        status.put("ollamaAvailable", ollamaAvailable);

        String activeProviderName = selectProvider().getProviderName();
        status.put("activeProvider", activeProviderName);

        return status;
    }

    private LlmProvider selectProvider() {
        if ("mock".equalsIgnoreCase(configuredProvider)) {
            return mockLlmProvider;
        }

        if (ollamaLlmProvider.isAvailable()) {
            return ollamaLlmProvider;
        }

        if (allowMockFallback) {
            return mockLlmProvider;
        }

        return ollamaLlmProvider;
    }
}
