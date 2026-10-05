package com.familytech.translator.controller;

import com.familytech.translator.dto.FamilyMemberRequest;
import com.familytech.translator.dto.FamilyMemberResponse;
import com.familytech.translator.service.FamilyMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/family-members")
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    public FamilyMemberController(FamilyMemberService familyMemberService) {
        this.familyMemberService = familyMemberService;
    }

    @GetMapping
    public ResponseEntity<List<FamilyMemberResponse>> getAllFamilyMembers() {
        return ResponseEntity.ok(familyMemberService.getAllFamilyMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyMemberResponse> getFamilyMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(familyMemberService.getFamilyMemberById(id));
    }

    @PostMapping
    public ResponseEntity<FamilyMemberResponse> createFamilyMember(@Valid @RequestBody FamilyMemberRequest request) {
        FamilyMemberResponse created = familyMemberService.createFamilyMember(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyMemberResponse> updateFamilyMember(
            @PathVariable Long id,
            @Valid @RequestBody FamilyMemberRequest request) {
        FamilyMemberResponse updated = familyMemberService.updateFamilyMember(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamilyMember(@PathVariable Long id) {
        familyMemberService.deleteFamilyMember(id);
        return ResponseEntity.noContent().build();
    }
}
