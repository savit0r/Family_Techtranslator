import React, { useState } from 'react';
import { ArrowRight } from 'lucide-react';

interface TranslatorFormProps {
  onTranslate: (concept: string) => Promise<void>;
  loading: boolean;
  disabled: boolean;
  selectedMemberName?: string;
}

const SUGGESTED_CONCEPTS = [
  'Kubernetes Pods',
  'Database Index',
  'REST API',
  'Recursion',
  'Docker Containers',
  'Cache Memory',
  'OAuth2 Login',
  'Git Branching',
];

export const TranslatorForm: React.FC<TranslatorFormProps> = ({
  onTranslate,
  loading,
  disabled,
  selectedMemberName,
}) => {
  const [concept, setConcept] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!concept.trim() || disabled || loading) return;
    await onTranslate(concept.trim());
  };

  const handleSelectSuggestion = (suggested: string) => {
    setConcept(suggested);
  };

  const ctaText = selectedMemberName
    ? `Explain for ${selectedMemberName}`
    : 'Explain Concept';

  return (
    <section className="card-surface" style={{ padding: '1.75rem', marginBottom: '2.5rem' }}>
      <label
        htmlFor="concept-input"
        style={{ display: 'block', fontSize: '1.15rem', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '0.85rem', letterSpacing: '-0.01em' }}
      >
        What do you want to explain?
      </label>

      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
        <div style={{ flex: 1, minWidth: '260px' }}>
          <input
            id="concept-input"
            type="text"
            className="input-field"
            placeholder={disabled ? 'Please pick someone above first...' : 'Try: How does Kubernetes keep an app running?'}
            value={concept}
            onChange={(e) => setConcept(e.target.value)}
            disabled={disabled || loading}
          />
        </div>

        <button
          type="submit"
          className="btn-primary"
          disabled={disabled || loading || !concept.trim()}
          style={{ minWidth: '160px' }}
        >
          {loading ? (
            'Explaining...'
          ) : (
            <>
              {ctaText} <ArrowRight className="cta-arrow" size={16} />
            </>
          )}
        </button>
      </form>

      {/* Suggested Concept Chips */}
      <div style={{ marginTop: '1.1rem', display: 'flex', alignItems: 'center', gap: '0.4rem', flexWrap: 'wrap' }}>
        <span style={{ fontSize: '0.8rem', color: 'var(--text-tertiary)', marginRight: '0.2rem', fontWeight: 500 }}>
          Suggestions:
        </span>
        {SUGGESTED_CONCEPTS.map((item) => (
          <button
            key={item}
            type="button"
            className="suggestion-chip"
            onClick={() => handleSelectSuggestion(item)}
            disabled={disabled || loading}
          >
            {item}
          </button>
        ))}
      </div>
    </section>
  );
};

