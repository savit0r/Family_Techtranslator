package com.familytech.translator.service;

import com.familytech.translator.dto.FamilyMemberRequest;
import com.familytech.translator.dto.FamilyMemberResponse;
import com.familytech.translator.exception.ResourceNotFoundException;
import com.familytech.translator.model.FamilyMember;
import com.familytech.translator.repository.FamilyMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FamilyMemberService {

    private final FamilyMemberRepository familyMemberRepository;

    public FamilyMemberService(FamilyMemberRepository familyMemberRepository) {
        this.familyMemberRepository = familyMemberRepository;
    }

    public List<FamilyMemberResponse> getAllFamilyMembers() {
        return familyMemberRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(FamilyMemberResponse::fromEntity)
                .toList();
    }

    public FamilyMemberResponse getFamilyMemberById(Long id) {
        FamilyMember member = findEntityById(id);
        return FamilyMemberResponse.fromEntity(member);
    }

    public FamilyMember findEntityById(Long id) {
        return familyMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FamilyMember", "id", id));
    }

    @Transactional
    public FamilyMemberResponse createFamilyMember(FamilyMemberRequest request) {
        FamilyMember member = new FamilyMember(
            request.name().trim(),
            request.relationship().trim(),
            request.occupation().trim(),
            request.interests().trim(),
            request.familiarTopics().trim(),
            request.techLevel(),
            request.preferredLanguage().trim(),
            request.communicationStyle().trim()
        );
        FamilyMember saved = familyMemberRepository.save(member);
        return FamilyMemberResponse.fromEntity(saved);
    }

    @Transactional
    public FamilyMemberResponse updateFamilyMember(Long id, FamilyMemberRequest request) {
        FamilyMember member = findEntityById(id);
        member.setName(request.name().trim());
        member.setRelationship(request.relationship().trim());
        member.setOccupation(request.occupation().trim());
        member.setInterests(request.interests().trim());
        member.setFamiliarTopics(request.familiarTopics().trim());
        member.setTechLevel(request.techLevel());
        member.setPreferredLanguage(request.preferredLanguage().trim());
        member.setCommunicationStyle(request.communicationStyle().trim());

        FamilyMember updated = familyMemberRepository.save(member);
        return FamilyMemberResponse.fromEntity(updated);
    }

    @Transactional
    public void deleteFamilyMember(Long id) {
        FamilyMember member = findEntityById(id);
        familyMemberRepository.delete(member);
    }
}
