package com.familytech.translator.service;

import com.familytech.translator.dto.FamilyMemberRequest;
import com.familytech.translator.dto.FamilyMemberResponse;
import com.familytech.translator.exception.ResourceNotFoundException;
import com.familytech.translator.model.FamilyMember;
import com.familytech.translator.model.TechLevel;
import com.familytech.translator.repository.FamilyMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyMemberServiceTest {

    @Mock
    private FamilyMemberRepository familyMemberRepository;

    @InjectMocks
    private FamilyMemberService familyMemberService;

    private FamilyMember sampleMember;

    @BeforeEach
    void setUp() {
        sampleMember = new FamilyMember(
            "Maria",
            "Grandmother",
            "Gardener",
            "Planting roses, baking bread",
            "Soil nutrients, pruning, flower seasonal cycles",
            TechLevel.BEGINNER,
            "Spanish",
            "Storyteller & Visual"
        );
        sampleMember.setId(1L);
    }

    @Test
    void getAllFamilyMembers_returnsListOfResponses() {
        when(familyMemberRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(sampleMember));

        List<FamilyMemberResponse> result = familyMemberService.getAllFamilyMembers();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Maria");
        assertThat(result.getFirst().relationship()).isEqualTo("Grandmother");
        assertThat(result.getFirst().familiarTopics()).contains("Soil nutrients");
        assertThat(result.getFirst().communicationStyle()).isEqualTo("Storyteller & Visual");
    }

    @Test
    void getFamilyMemberById_whenFound_returnsResponse() {
        when(familyMemberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));

        FamilyMemberResponse result = familyMemberService.getFamilyMemberById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Maria");
        assertThat(result.familiarTopics()).contains("pruning");
    }

    @Test
    void getFamilyMemberById_whenNotFound_throwsException() {
        when(familyMemberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> familyMemberService.getFamilyMemberById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("FamilyMember not found");
    }

    @Test
    void createFamilyMember_validRequest_savesAndReturnsResponse() {
        FamilyMemberRequest request = new FamilyMemberRequest(
            "Maria",
            "Grandmother",
            "Gardener",
            "Planting roses, baking bread",
            "Soil nutrients, pruning, flower seasonal cycles",
            TechLevel.BEGINNER,
            "Spanish",
            "Storyteller & Visual"
        );

        when(familyMemberRepository.save(any(FamilyMember.class))).thenReturn(sampleMember);

        FamilyMemberResponse result = familyMemberService.createFamilyMember(request);

        assertThat(result.name()).isEqualTo("Maria");
        assertThat(result.communicationStyle()).isEqualTo("Storyteller & Visual");
        verify(familyMemberRepository, times(1)).save(any(FamilyMember.class));
    }

    @Test
    void deleteFamilyMember_whenFound_deletesMember() {
        when(familyMemberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));

        familyMemberService.deleteFamilyMember(1L);

        verify(familyMemberRepository, times(1)).delete(sampleMember);
    }
}
