package com.familytech.translator.repository;

import com.familytech.translator.model.TranslationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationHistoryRepository extends JpaRepository<TranslationHistory, Long> {
    List<TranslationHistory> findByFamilyMemberIdOrderByCreatedAtDesc(Long familyMemberId);
    List<TranslationHistory> findAllByOrderByCreatedAtDesc();
}
