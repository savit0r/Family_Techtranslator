import type { ExplainBackRequest, ExplainBackResponse, FamilyMember, FamilyMemberRequest, SystemStatus, TranslationRequest, TranslationResponse } from './types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://familytech-translator-backend.onrender.com/api/v1';

async function fetchJson<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
    ...options,
  });

  if (!response.ok) {
    let errorMessage = `HTTP Error ${response.status}`;
    try {
      const errorData = await response.json();
      if (errorData.message) {
        errorMessage = errorData.message;
      }
    } catch {
      // Use fallback error message
    }
    throw new Error(errorMessage);
  }

  if (response.status === 204) {
    return {} as T;
  }

  return response.json();
}

export const api = {
  // Family Members API
  getFamilyMembers: (): Promise<FamilyMember[]> =>
    fetchJson<FamilyMember[]>(`${API_BASE_URL}/family-members`),

  createFamilyMember: (data: FamilyMemberRequest): Promise<FamilyMember> =>
    fetchJson<FamilyMember>(`${API_BASE_URL}/family-members`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  updateFamilyMember: (id: number, data: FamilyMemberRequest): Promise<FamilyMember> =>
    fetchJson<FamilyMember>(`${API_BASE_URL}/family-members/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  deleteFamilyMember: (id: number): Promise<void> =>
    fetchJson<void>(`${API_BASE_URL}/family-members/${id}`, {
      method: 'DELETE',
    }),

  // Translation Engine API
  translateConcept: (data: TranslationRequest): Promise<TranslationResponse> =>
    fetchJson<TranslationResponse>(`${API_BASE_URL}/translations`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  submitFeedback: (translationId: number, feedback: 'helpful' | 'not-helpful'): Promise<TranslationResponse> =>
    fetchJson<TranslationResponse>(`${API_BASE_URL}/translations/${translationId}/feedback`, {
      method: 'POST',
      body: JSON.stringify({ feedback }),
    }),

  evaluateExplainBack: (data: ExplainBackRequest): Promise<ExplainBackResponse> =>
    fetchJson<ExplainBackResponse>(`${API_BASE_URL}/translations/${data.translationId}/explain-back`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  getTranslationHistory: (familyMemberId: number): Promise<TranslationResponse[]> =>
    fetchJson<TranslationResponse[]>(`${API_BASE_URL}/translations/history?familyMemberId=${familyMemberId}`),

  // System & AI Status API
  getSystemStatus: (): Promise<SystemStatus> =>
    fetchJson<SystemStatus>(`${API_BASE_URL}/ai/status`),
};
