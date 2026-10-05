package com.familytech.translator.service.ai;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.exception.LlmProviderUnavailableException;
import com.familytech.translator.exception.LlmTimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OllamaLlmProvider implements LlmProvider {

    private final RestClient restClient;
    private final String baseUrl;
    private final String model;

    public OllamaLlmProvider(
            @Value("${app.llm.ollama.base-url:http://localhost:11434}") String baseUrl,
            @Value("${app.llm.ollama.model:llama3.2:3b}") String model,
            @Value("${app.llm.ollama.connect-timeout-ms:5000}") int connectTimeout,
            @Value("${app.llm.ollama.read-timeout-ms:60000}") int readTimeout) {
        this.baseUrl = baseUrl;
        this.model = model;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public AiGenerateResponse generate(AiGenerateRequest request) {
        long startTime = System.currentTimeMillis();

        Map<String, Object> options = new HashMap<>();
        if (request.temperature() != null) {
            options.put("temperature", request.temperature());
        }
        if (request.maxTokens() != null) {
            options.put("num_predict", request.maxTokens());
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", request.prompt());
        if (request.systemPrompt() != null && !request.systemPrompt().isBlank()) {
            requestBody.put("system", request.systemPrompt());
        }
        requestBody.put("stream", false);
        if (!options.isEmpty()) {
            requestBody.put("options", options);
        }

        try {
            OllamaResponse response = restClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(OllamaResponse.class);

            long duration = System.currentTimeMillis() - startTime;

            if (response == null || response.response() == null) {
                throw new LlmProviderUnavailableException("Received empty response from local Ollama model");
            }

            return new AiGenerateResponse(
                    response.response().trim(),
                    response.model() != null ? response.model() : model,
                    getProviderName(),
                    duration,
                    false
            );

        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new LlmTimeoutException("Ollama model generation timed out", e);
            }
            throw new LlmProviderUnavailableException("Could not connect to local Ollama server at " + baseUrl + ". Is Ollama running?", e);
        } catch (Exception e) {
            throw new LlmProviderUnavailableException("Ollama generation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            Map<?, ?> tags = restClient.get()
                    .uri("/api/tags")
                    .retrieve()
                    .body(Map.class);
            return tags != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getProviderName() {
        return "ollama";
    }

    public record OllamaResponse(
            String model,
            String response,
            boolean done,
            Long totalDuration
    ) {}
}
