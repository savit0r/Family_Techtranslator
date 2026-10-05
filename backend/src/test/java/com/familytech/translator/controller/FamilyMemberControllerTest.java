package com.familytech.translator.controller;

import com.familytech.translator.dto.FamilyMemberRequest;
import com.familytech.translator.dto.FamilyMemberResponse;
import com.familytech.translator.exception.ResourceNotFoundException;
import com.familytech.translator.model.TechLevel;
import com.familytech.translator.service.FamilyMemberService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FamilyMemberController.class)
class FamilyMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FamilyMemberService familyMemberService;

    @Test
    void getAllFamilyMembers_returnsOkAndList() throws Exception {
        FamilyMemberResponse member = new FamilyMemberResponse(
            1L, "Maria", "Grandmother", "Gardener", "Planting roses", "Soil nutrients", TechLevel.BEGINNER, "Spanish", "Storyteller", ZonedDateTime.now(), ZonedDateTime.now()
        );

        when(familyMemberService.getAllFamilyMembers()).thenReturn(List.of(member));

        mockMvc.perform(get("/api/v1/family-members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Maria"))
                .andExpect(jsonPath("$[0].relationship").value("Grandmother"))
                .andExpect(jsonPath("$[0].familiarTopics").value("Soil nutrients"))
                .andExpect(jsonPath("$[0].communicationStyle").value("Storyteller"));
    }

    @Test
    void createFamilyMember_validRequest_returns201Created() throws Exception {
        FamilyMemberRequest request = new FamilyMemberRequest(
            "Maria", "Grandmother", "Gardener", "Planting roses", "Soil nutrients", TechLevel.BEGINNER, "Spanish", "Storyteller"
        );

        FamilyMemberResponse response = new FamilyMemberResponse(
            1L, "Maria", "Grandmother", "Gardener", "Planting roses", "Soil nutrients", TechLevel.BEGINNER, "Spanish", "Storyteller", ZonedDateTime.now(), ZonedDateTime.now()
        );

        when(familyMemberService.createFamilyMember(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/family-members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.familiarTopics").value("Soil nutrients"))
                .andExpect(jsonPath("$.communicationStyle").value("Storyteller"));
    }

    @Test
    void createFamilyMember_invalidRequest_returns400BadRequest() throws Exception {
        // Missing required fields
        FamilyMemberRequest request = new FamilyMemberRequest(
            "", "", "Gardener", "Planting roses", "", TechLevel.BEGINNER, "Spanish", ""
        );

        mockMvc.perform(post("/api/v1/family-members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.relationship").exists())
                .andExpect(jsonPath("$.fieldErrors.familiarTopics").exists())
                .andExpect(jsonPath("$.fieldErrors.communicationStyle").exists());
    }

    @Test
    void getFamilyMemberById_notFound_returns404() throws Exception {
        when(familyMemberService.getFamilyMemberById(99L))
                .thenThrow(new ResourceNotFoundException("FamilyMember", "id", 99L));

        mockMvc.perform(get("/api/v1/family-members/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("FamilyMember not found with id : '99'"));
    }
}
