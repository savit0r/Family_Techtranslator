package com.familytech.translator.controller;

import com.familytech.translator.dto.TranslationRequest;
import com.familytech.translator.dto.TranslationResponse;
import com.familytech.translator.service.TranslationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslationController.class)
class TranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TranslationService translationService;

    @Test
    void translateConcept_validRequest_returns200Ok() throws Exception {
        TranslationRequest request = new TranslationRequest(1L, "Database Index");

        TranslationResponse response = new TranslationResponse(
            1L, 1L, "Maria", "Database Index", "Summary text", "Breakdown text",
            "Mapping text", "Takeaway text", "Followup question", null, "Full explanation text",
            "llama3.2:3b", "ollama", 250L, ZonedDateTime.now()
        );

        when(translationService.translateConcept(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/translations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.concept").value("Database Index"))
                .andExpect(jsonPath("$.familyMemberName").value("Maria"))
                .andExpect(jsonPath("$.analogySummary").value("Summary text"));
    }

    @Test
    void getHistoryForFamilyMember_returnsHistoryList() throws Exception {
        TranslationResponse response = new TranslationResponse(
            1L, 1L, "Maria", "Database Index", "Summary text", "Breakdown text",
            "Mapping text", "Takeaway text", "Followup question", null, "Full explanation text",
            "llama3.2:3b", "ollama", 250L, ZonedDateTime.now()
        );

        when(translationService.getHistoryForFamilyMember(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/translations/history").param("familyMemberId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].concept").value("Database Index"))
                .andExpect(jsonPath("$[0].familyMemberName").value("Maria"));
    }

    @Test
    void evaluateExplainBack_validRequest_returns200Ok() throws Exception {
        com.familytech.translator.dto.ExplainBackRequest request = new com.familytech.translator.dto.ExplainBackRequest(1L, "It is like a book index.");
        com.familytech.translator.dto.ExplainBackResponse response = new com.familytech.translator.dto.ExplainBackResponse(
            1L, "Database Index", "It is like a book index.",
            "Understood fast lookup.", "No major gaps.", "Great job!",
            "Full text", "llama3.2:3b", "ollama", 150L
        );

        when(translationService.evaluateExplainBack(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/translations/1/explain-back")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translationId").value(1L))
                .andExpect(jsonPath("$.concept").value("Database Index"))
                .andExpect(jsonPath("$.whatTheyUnderstood").value("Understood fast lookup."))
                .andExpect(jsonPath("$.misunderstandings").value("No major gaps."))
                .andExpect(jsonPath("$.shortClarification").value("Great job!"));
    }
}
