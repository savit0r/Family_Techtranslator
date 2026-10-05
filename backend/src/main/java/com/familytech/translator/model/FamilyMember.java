package com.familytech.translator.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "family_members")
public class FamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String relationship;

    @Column(nullable = false, length = 100)
    private String occupation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String interests;

    @Column(name = "familiar_topics", nullable = false, columnDefinition = "TEXT")
    private String familiarTopics;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_level", nullable = false, length = 30)
    private TechLevel techLevel;

    @Column(name = "preferred_language", nullable = false, length = 50)
    private String preferredLanguage;

    @Column(name = "communication_style", nullable = false, length = 100)
    private String communicationStyle;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    public FamilyMember() {
    }

    public FamilyMember(String name, String relationship, String occupation, String interests, String familiarTopics, TechLevel techLevel, String preferredLanguage, String communicationStyle) {
        this.name = name;
        this.relationship = relationship;
        this.occupation = occupation;
        this.interests = interests;
        this.familiarTopics = familiarTopics != null ? familiarTopics : "";
        this.techLevel = techLevel;
        this.preferredLanguage = preferredLanguage != null ? preferredLanguage : "English";
        this.communicationStyle = communicationStyle != null ? communicationStyle : "Direct & Practical";
    }

    @PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getFamiliarTopics() {
        return familiarTopics;
    }

    public void setFamiliarTopics(String familiarTopics) {
        this.familiarTopics = familiarTopics;
    }

    public TechLevel getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(TechLevel techLevel) {
        this.techLevel = techLevel;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getCommunicationStyle() {
        return communicationStyle;
    }

    public void setCommunicationStyle(String communicationStyle) {
        this.communicationStyle = communicationStyle;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
