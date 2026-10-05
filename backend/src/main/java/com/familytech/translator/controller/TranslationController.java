package com.familytech.translator.controller;

import com.familytech.translator.dto.ExplainBackRequest;
import com.familytech.translator.dto.ExplainBackResponse;
import com.familytech.translator.dto.FeedbackRequest;
import com.familytech.translator.dto.TranslationRequest;
import com.familytech.translator.dto.TranslationResponse;
import com.familytech.translator.service.TranslationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/translations")
public class TranslationController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public ResponseEntity<TranslationResponse> translateConcept(@Valid @RequestBody TranslationRequest request) {
        TranslationResponse response = translationService.translateConcept(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<TranslationResponse> recordFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request) {
        TranslationResponse response = translationService.recordFeedback(id, request.feedback());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/explain-back")
    public ResponseEntity<ExplainBackResponse> evaluateExplainBack(
            @PathVariable Long id,
            @Valid @RequestBody ExplainBackRequest request) {
        ExplainBackResponse response = translationService.evaluateExplainBack(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TranslationResponse>> getHistoryForFamilyMember(@RequestParam Long familyMemberId) {
        List<TranslationResponse> history = translationService.getHistoryForFamilyMember(familyMemberId);
        return ResponseEntity.ok(history);
    }
}
