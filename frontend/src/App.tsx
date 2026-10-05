import { useState, useEffect, useCallback } from 'react';
import { Header } from './components/Header';
import { ProfileSelector } from './components/ProfileSelector';
import { ProfileModal } from './components/ProfileModal';
import { TranslatorForm } from './components/TranslatorForm';
import { ExplanationView } from './components/ExplanationView';
import { HistoryDrawer } from './components/HistoryDrawer';
import { api } from './api';
import type { FamilyMember, FamilyMemberRequest, SystemStatus, TranslationResponse } from './types';

export function App() {
  const [theme, setTheme] = useState<'light' | 'dark'>(() => {
    const saved = localStorage.getItem('familytech_theme');
    if (saved === 'light' || saved === 'dark') return saved;
    return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  });

  const [members, setMembers] = useState<FamilyMember[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [status, setStatus] = useState<SystemStatus | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [currentTranslation, setCurrentTranslation] = useState<TranslationResponse | null>(null);
  const [history, setHistory] = useState<TranslationResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Apply theme to root document
  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('familytech_theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prev) => (prev === 'dark' ? 'light' : 'dark'));
  };

  // Load initial system status and family members
  useEffect(() => {
    const initData = async () => {
      try {
        const [statusData, membersData] = await Promise.all([
          api.getSystemStatus().catch(() => null),
          api.getFamilyMembers().catch(() => []),
        ]);

        setStatus(statusData);
        setMembers(membersData);

        if (membersData.length > 0) {
          setSelectedId(membersData[0].id);
        }
      } catch {
        setError('Failed to connect to backend server. Make sure Spring Boot backend is running on http://localhost:8080.');
      }
    };

    initData();
  }, []);

  // Fetch history whenever selected family member changes
  const loadHistory = useCallback(async (memberId: number) => {
    try {
      const historyData = await api.getTranslationHistory(memberId);
      setHistory(historyData);
    } catch {
      setHistory([]);
    }
  }, []);

  useEffect(() => {
    if (selectedId) {
      loadHistory(selectedId);
    } else {
      setHistory([]);
    }
  }, [selectedId, loadHistory]);

  const handleSelectMember = (id: number) => {
    setSelectedId(id);
    setCurrentTranslation(null);
    setError(null);
  };

  const handleCreateMember = async (data: FamilyMemberRequest) => {
    const created = await api.createFamilyMember(data);
    setMembers((prev) => [created, ...prev]);
    setSelectedId(created.id);
  };

  const handleDeleteMember = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this profile?')) return;
    await api.deleteFamilyMember(id);
    setMembers((prev) => prev.filter((m) => m.id !== id));
    if (selectedId === id) {
      const remaining = members.filter((m) => m.id !== id);
      setSelectedId(remaining.length > 0 ? remaining[0].id : null);
      setCurrentTranslation(null);
    }
  };

  const handleTranslate = async (concept: string) => {
    if (!selectedId) {
      setError('Please select a family member first.');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await api.translateConcept({
        familyMemberId: selectedId,
        concept,
      });

      setCurrentTranslation(response);
      setHistory((prev) => [response, ...prev]);
    } catch (err: any) {
      setError(err.message || 'Failed to generate explanation from local model.');
    } finally {
      setLoading(false);
    }
  };

  const handleFeedbackSubmit = async (translationId: number, feedback: 'helpful' | 'not-helpful') => {
    try {
      const updated = await api.submitFeedback(translationId, feedback);
      setCurrentTranslation(updated);
      setHistory((prev) => prev.map((item) => (item.id === translationId ? updated : item)));
    } catch {
      // Fallback
    }
  };

  const selectedMember = members.find((m) => m.id === selectedId) || null;

  return (
    <div className="container">
      <Header status={status} theme={theme} onToggleTheme={toggleTheme} />

      <main style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
        <ProfileSelector
          members={members}
          selectedId={selectedId}
          onSelect={handleSelectMember}
          onOpenCreateModal={() => setIsModalOpen(true)}
          onDeleteMember={handleDeleteMember}
        />

        <TranslatorForm
          onTranslate={handleTranslate}
          loading={loading}
          disabled={!selectedId}
          selectedMemberName={selectedMember?.name}
        />

        <ExplanationView
          translation={currentTranslation}
          selectedMember={selectedMember}
          loading={loading}
          error={error}
          onFeedbackSubmit={handleFeedbackSubmit}
        />

        <HistoryDrawer
          history={history}
          onSelectHistoryItem={(item) => setCurrentTranslation(item)}
        />
      </main>

      <ProfileModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleCreateMember}
      />
    </div>
  );
}

export default App;

