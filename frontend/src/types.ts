export type TechLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';

export interface FamilyMember {
  id: number;
  name: string;
  relationship: string;
  occupation: string;
  interests: string;
  familiarTopics: string;
  techLevel: TechLevel;
  preferredLanguage: string;
  communicationStyle: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface FamilyMemberRequest {
  name: string;
  relationship: string;
  occupation: string;
  interests: string;
  familiarTopics: string;
  techLevel: TechLevel;
  preferredLanguage: string;
  communicationStyle: string;
}

export interface TranslationRequest {
  familyMemberId: number;
  concept: string;
}

export interface TranslationResponse {
  id: number;
  familyMemberId: number;
  familyMemberName: string;
  concept: string;
  analogySummary: string;
  conceptBreakdown: string;
  analogyToTechMapping: string;
  keyTakeaway: string;
  followUpQuestion?: string;
  feedback?: string | null;
  fullExplanation: string;
  modelUsed: string;
  providerUsed: string;
  generationTimeMs: number;
  createdAt: string;
}

export interface SystemStatus {
  configuredProvider: string;
  allowMockFallback: boolean;
  ollamaAvailable: boolean;
  activeProvider: string;
}

export interface ExplainBackRequest {
  translationId: number;
  userExplanation: string;
}

export interface ExplainBackResponse {
  translationId: number;
  concept: string;
  userExplanation: string;
  whatTheyUnderstood: string;
  misunderstandings: string;
  shortClarification: string;
  fullEvaluationText: string;
  modelUsed: string;
  providerUsed: string;
  generationTimeMs: number;
}
