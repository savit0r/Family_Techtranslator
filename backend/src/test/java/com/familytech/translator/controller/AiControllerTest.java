package com.familytech.translator.controller;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import com.familytech.translator.service.ai.AiGenerationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AiGenerationService aiGenerationService;

    @Test
    void generate_validRequest_returns200Ok() throws Exception {
        AiGenerateRequest request = new AiGenerateRequest("Explain REST API", "System prompt", 0.7, 300);
        AiGenerateResponse response = new AiGenerateResponse("Analogy response text", "llama3.2:3b", "ollama", 200L, false);

        when(aiGenerationService.generate(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ai/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Analogy response text"))
                .andExpect(jsonPath("$.provider").value("ollama"))
                .andExpect(jsonPath("$.mock").value(false));
    }

    @Test
    void generate_blankPrompt_returns400BadRequest() throws Exception {
        AiGenerateRequest request = new AiGenerateRequest("", null, null, null);

        mockMvc.perform(post("/api/v1/ai/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.prompt").exists());
    }

    @Test
    void getStatus_returnsStatusInfo() throws Exception {
        Map<String, Object> statusMap = Map.of(
            "configuredProvider", "ollama",
            "ollamaAvailable", false,
            "activeProvider", "mock"
        );

        when(aiGenerationService.getStatus()).thenReturn(statusMap);

        mockMvc.perform(get("/api/v1/ai/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configuredProvider").value("ollama"))
                .andExpect(jsonPath("$.activeProvider").value("mock"));
    }
}
