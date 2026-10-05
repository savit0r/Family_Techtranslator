import React, { useState, useEffect } from 'react';
import { ThumbsUp, ThumbsDown, Copy, Check, Sparkles } from 'lucide-react';
import type { TranslationResponse, FamilyMember, ExplainBackResponse } from '../types';
import { api } from '../api';

interface ExplanationViewProps {
  translation: TranslationResponse | null;
  selectedMember: FamilyMember | null;
  loading: boolean;
  error: string | null;
  onFeedbackSubmit?: (translationId: number, feedback: 'helpful' | 'not-helpful') => Promise<void>;
}

export const ExplanationView: React.FC<ExplanationViewProps> = ({
  translation,
  selectedMember,
  loading,
  error,
  onFeedbackSubmit,
}) => {
  const [feedback, setFeedback] = useState<'helpful' | 'not-helpful' | null>(null);
  const [isSubmittingFeedback, setIsSubmittingFeedback] = useState(false);
  const [copied, setCopied] = useState(false);

  // Explain-Back Learning Loop state
  const [userExplanation, setUserExplanation] = useState('');
  const [evalResult, setEvalResult] = useState<ExplainBackResponse | null>(null);
  const [isEvaluating, setIsEvaluating] = useState(false);
  const [explainBackError, setExplainBackError] = useState<string | null>(null);

  useEffect(() => {
    if (translation) {
      if (translation.feedback === 'helpful' || translation.feedback === 'not-helpful') {
        setFeedback(translation.feedback);
      } else {
        setFeedback(null);
      }
      setUserExplanation('');
      setEvalResult(null);
      setExplainBackError(null);
    }
  }, [translation?.id, translation?.feedback]);

  const handleFeedback = async (type: 'helpful' | 'not-helpful') => {
    if (!translation || isSubmittingFeedback) return;
    setIsSubmittingFeedback(true);
    setFeedback(type);
    try {
      if (onFeedbackSubmit) {
        await onFeedbackSubmit(translation.id, type);
      }
    } catch {
      // Keep local state
    } finally {
      setIsSubmittingFeedback(false);
    }
  };

  const handleCopyQuestion = (text: string) => {
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleExplainBackSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!translation || !userExplanation.trim() || isEvaluating) return;

    try {
      setIsEvaluating(true);
      setExplainBackError(null);
      const response = await api.evaluateExplainBack({
        translationId: translation.id,
        userExplanation: userExplanation.trim(),
      });
      setEvalResult(response);
    } catch (err: any) {
      setExplainBackError(err.message || 'Failed to evaluate explain-back response.');
    } finally {
      setIsEvaluating(false);
    }
  };

  if (loading) {
    const memberName = selectedMember?.name || 'your family member';
    return (
      <section
        className="card-surface"
        style={{ padding: '3.5rem 2rem', textAlign: 'center' }}
        aria-live="polite"
        aria-busy="true"
      >
        <div style={{ maxWidth: '440px', margin: '0 auto', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1.25rem' }}>
          <div style={{
            width: '48px',
            height: '48px',
            borderRadius: '50%',
            backgroundColor: 'var(--accent-subtle)',
            color: 'var(--accent-primary)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}>
            <Sparkles size={22} />
          </div>

          <div>
            <h3 style={{ fontSize: '1.15rem', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
              Explaining this in {memberName}'s world...
            </h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.88rem', lineHeight: 1.5 }}>
              Connecting technical principles with {selectedMember?.occupation || 'their background'}.
            </p>
          </div>

          <div style={{ display: 'flex', gap: '0.5rem', width: '100%', justifyContent: 'center', marginTop: '0.5rem' }}>
            <div className="skeleton" style={{ width: '30%', height: '8px' }} />
            <div className="skeleton" style={{ width: '40%', height: '8px' }} />
            <div className="skeleton" style={{ width: '25%', height: '8px' }} />
          </div>
        </div>
      </section>
    );
  }

  if (error) {
    return (
      <section
        className="card-surface"
        style={{
          padding: '1.75rem',
          backgroundColor: 'var(--accent-error-bg)',
          borderColor: 'var(--accent-error-border)'
        }}
        role="alert"
      >
        <h3 style={{ color: 'var(--accent-error)', fontSize: '1.05rem', fontWeight: 700, marginBottom: '0.4rem' }}>
          Unable to Generate Explanation
        </h3>
        <p style={{ color: 'var(--text-secondary)', fontSize: '0.92rem' }}>{error}</p>
      </section>
    );
  }

  if (!translation) {
    return (
      <section className="card-surface" style={{ padding: '3.5rem 2rem', textAlign: 'center', borderStyle: 'dashed' }}>
        <h3 style={{ fontSize: '1.15rem', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '0.4rem' }}>
          Ready to Explain
        </h3>
        <p style={{ color: 'var(--text-secondary)', maxWidth: '460px', margin: '0 auto', fontSize: '0.92rem' }}>
          Pick someone from your family above, then give me a technical concept to explain.
        </p>
      </section>
    );
  }

  const followUpQuestionText = translation.followUpQuestion ||
    `How does ${translation.concept} handle scaling or high workloads in ${selectedMember?.occupation || 'their field'}?`;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
      {/* Content Container with reveal animation */}
      <article className="card-surface animate-reveal" style={{ padding: '2.25rem 2rem' }}>
        {/* Editorial Header */}
        <header style={{ borderBottom: '1px solid var(--border-subtle)', paddingBottom: '1.25rem', marginBottom: '1.75rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.35rem' }}>
            <span style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--accent-primary)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>
              EXPLAINED FOR {translation.familyMemberName.toUpperCase()}
            </span>
            {selectedMember?.relationship && (
              <span style={{ fontSize: '0.78rem', color: 'var(--text-tertiary)' }}>
                · {selectedMember.relationship} ({selectedMember.occupation})
              </span>
            )}
          </div>

          <h2 style={{ fontSize: '1.75rem', fontWeight: 700, color: 'var(--text-primary)', letterSpacing: '-0.025em', lineHeight: 1.3 }}>
            {translation.concept}
          </h2>

          <div style={{ fontSize: '0.78rem', color: 'var(--text-tertiary)', marginTop: '0.4rem', display: 'flex', gap: '1rem' }}>
            <span>Model: {translation.modelUsed}</span>
            <span>Generated in {translation.generationTimeMs}ms</span>
          </div>
        </header>

        {/* 1. Main Analogy (Visually Dominant) */}
        <section style={{ marginBottom: '2.25rem' }}>
          <h3 style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: '0.75rem' }}>
            Think of it like this...
          </h3>
          <div style={{
            padding: '1.5rem 1.65rem',
            backgroundColor: 'var(--accent-subtle)',
            borderLeft: '4px solid var(--accent-primary)',
            borderRadius: '0 var(--radius-md) var(--radius-md) 0'
          }}>
            <p style={{ fontSize: '1.12rem', fontWeight: 600, color: 'var(--text-primary)', lineHeight: 1.6 }}>
              {translation.analogySummary}
            </p>
          </div>
        </section>

        {/* 2. Step-by-Step Explanation */}
        <section style={{ marginBottom: '2.25rem' }}>
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '0.85rem' }}>
            Why This Makes Sense
          </h3>
          <div style={{
            fontSize: '0.96rem',
            lineHeight: 1.7,
            color: 'var(--text-secondary)',
            whiteSpace: 'pre-wrap'
          }}>
            {translation.conceptBreakdown}
          </div>
        </section>

        {/* 3. The Technical Version */}
        <section style={{ marginBottom: '2.25rem' }}>
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '0.85rem' }}>
            The Technical Version
          </h3>
          <div style={{
            padding: '1.15rem 1.35rem',
            backgroundColor: 'var(--bg-surface-subtle)',
            border: '1px solid var(--border-subtle)',
            borderRadius: 'var(--radius-md)',
            fontSize: '0.92rem',
            lineHeight: 1.65,
            color: 'var(--text-primary)',
            whiteSpace: 'pre-wrap'
          }}>
            {translation.analogyToTechMapping}
          </div>
        </section>

        {/* 4. Suggested Follow-up Question */}
        <section style={{
          padding: '1.25rem 1.35rem',
          backgroundColor: 'var(--bg-surface-subtle)',
          border: '1px solid var(--border-subtle)',
          borderRadius: 'var(--radius-md)',
          marginBottom: '2.25rem'
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.4rem', flexWrap: 'wrap', gap: '0.5rem' }}>
            <h3 style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>
              Check Understanding (Ask {translation.familyMemberName})
            </h3>
            <button
              className="btn-secondary"
              onClick={() => handleCopyQuestion(followUpQuestionText)}
              style={{ padding: '0.25rem 0.65rem', fontSize: '0.78rem' }}
              title="Copy question"
            >
              {copied ? (
                <>
                  <Check size={13} color="var(--accent-success)" /> Copied
                </>
              ) : (
                <>
                  <Copy size={13} /> Copy Question
                </>
              )}
            </button>
          </div>
          <p style={{ fontSize: '0.98rem', color: 'var(--text-primary)', fontStyle: 'italic', fontWeight: 500, lineHeight: 1.5 }}>
            "{followUpQuestionText}"
          </p>
        </section>

        {/* 5. Explain-Back Challenge */}
        <section style={{
          padding: '1.5rem',
          backgroundColor: 'var(--bg-app)',
          border: '1px solid var(--border-subtle)',
          borderRadius: 'var(--radius-md)',
          marginBottom: '1.75rem'
        }}>
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
            Explain-Back Learning Loop
          </h3>
          <p style={{ fontSize: '0.88rem', color: 'var(--text-secondary)', marginBottom: '1rem' }}>
            Ask <strong>{translation.familyMemberName}</strong> to explain <strong>{translation.concept}</strong> in their own words. Type their answer below to evaluate educational understanding.
          </p>

          <form onSubmit={handleExplainBackSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
            <textarea
              className="input-field"
              rows={3}
              placeholder={`e.g., "${translation.familyMemberName} said: ${translation.concept} works like having a catalog index..."`}
              value={userExplanation}
              onChange={(e) => setUserExplanation(e.target.value)}
              disabled={isEvaluating}
              style={{ resize: 'vertical', minHeight: '80px' }}
            />

            <button
              type="submit"
              className="btn-primary"
              disabled={isEvaluating || !userExplanation.trim()}
              style={{ alignSelf: 'flex-start' }}
            >
              {isEvaluating ? 'Evaluating Understanding...' : 'Evaluate Understanding'}
            </button>
          </form>

          {explainBackError && (
            <div style={{ marginTop: '0.75rem', color: 'var(--accent-error)', fontSize: '0.88rem' }}>
              {explainBackError}
            </div>
          )}

          {evalResult && (
            <div style={{ marginTop: '1.5rem', display: 'flex', flexDirection: 'column', gap: '0.85rem', borderTop: '1px solid var(--border-subtle)', paddingTop: '1.25rem' }}>
              <h4 style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-primary)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>
                Educational Evaluation
              </h4>

              <div style={{ padding: '0.85rem 1rem', borderRadius: 'var(--radius-sm)', backgroundColor: 'var(--accent-success-bg)', border: '1px solid var(--accent-success-border)' }}>
                <h5 style={{ fontSize: '0.78rem', color: 'var(--accent-success)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.25rem' }}>
                  What They Understood
                </h5>
                <div style={{ fontSize: '0.9rem', color: 'var(--text-primary)', whiteSpace: 'pre-wrap', lineHeight: 1.55 }}>
                  {evalResult.whatTheyUnderstood}
                </div>
              </div>

              <div style={{ padding: '0.85rem 1rem', borderRadius: 'var(--radius-sm)', backgroundColor: 'var(--accent-warning-bg)', border: '1px solid var(--accent-warning-border)' }}>
                <h5 style={{ fontSize: '0.78rem', color: 'var(--accent-warning)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.25rem' }}>
                  Misunderstandings & Gaps
                </h5>
                <div style={{ fontSize: '0.9rem', color: 'var(--text-primary)', whiteSpace: 'pre-wrap', lineHeight: 1.55 }}>
                  {evalResult.misunderstandings}
                </div>
              </div>

              <div style={{ padding: '0.85rem 1rem', borderRadius: 'var(--radius-sm)', backgroundColor: 'var(--accent-subtle)', border: '1px solid var(--border-focus)' }}>
                <h5 style={{ fontSize: '0.78rem', color: 'var(--accent-primary)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.25rem' }}>
                  Short Clarification
                </h5>
                <p style={{ fontSize: '0.92rem', color: 'var(--text-primary)', lineHeight: 1.55, fontWeight: 500 }}>
                  {evalResult.shortClarification}
                </p>
              </div>
            </div>
          )}
        </section>

        {/* Feedback Control */}
        <footer style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          borderTop: '1px solid var(--border-subtle)',
          paddingTop: '1.25rem',
          flexWrap: 'wrap',
          gap: '1rem'
        }}>
          <span style={{ fontSize: '0.88rem', color: 'var(--text-secondary)', fontWeight: 500 }}>
            Was this explanation helpful for {translation.familyMemberName}?
          </span>

          {feedback ? (
            <span style={{ fontSize: '0.88rem', color: 'var(--accent-success)', fontWeight: 600 }}>
              Feedback recorded ({feedback === 'helpful' ? 'Helpful 👍' : 'Not Helpful 👎'})
            </span>
          ) : (
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                className="btn-secondary"
                onClick={() => handleFeedback('helpful')}
                disabled={isSubmittingFeedback}
                aria-label="Mark analogy as helpful"
              >
                <ThumbsUp size={15} /> Helpful
              </button>
              <button
                className="btn-secondary"
                onClick={() => handleFeedback('not-helpful')}
                disabled={isSubmittingFeedback}
                aria-label="Mark analogy as not helpful"
              >
                <ThumbsDown size={15} /> Not Helpful
              </button>
            </div>
          )}
        </footer>
      </article>
    </div>
  );
};

