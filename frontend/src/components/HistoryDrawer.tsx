import React from 'react';
import { History, Clock, ChevronRight } from 'lucide-react';
import type { TranslationResponse } from '../types';

interface HistoryDrawerProps {
  history: TranslationResponse[];
  onSelectHistoryItem: (item: TranslationResponse) => void;
}

export const HistoryDrawer: React.FC<HistoryDrawerProps> = ({
  history,
  onSelectHistoryItem,
}) => {
  if (history.length === 0) return null;

  return (
    <section className="card" style={{ padding: '1.25rem 1.5rem', marginTop: '2.5rem' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1rem' }}>
        <h3 style={{ fontSize: '0.95rem', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-primary)' }}>
          <History size={16} color="var(--accent-primary)" />
          Recent Explanations ({history.length})
        </h3>
        <span style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>Click to reload</span>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
        {history.map((item) => (
          <div
            key={item.id}
            onClick={() => onSelectHistoryItem(item)}
            role="button"
            tabIndex={0}
            onKeyDown={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                onSelectHistoryItem(item);
              }
            }}
            style={{
              padding: '0.875rem 1rem',
              borderRadius: 'var(--radius-sm)',
              background: 'var(--bg-canvas)',
              border: '1px solid var(--border-color)',
              cursor: 'pointer',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              transition: 'border-color 0.15s ease, background 0.15s ease'
            }}
          >
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <h4 style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-primary)' }}>
                  {item.concept}
                </h4>
                <span style={{ fontSize: '0.72rem', background: 'var(--accent-light)', color: 'var(--accent-primary)', padding: '0.1rem 0.4rem', borderRadius: '4px', fontWeight: 500 }}>
                  For {item.familyMemberName}
                </span>
              </div>
              <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)', marginTop: '0.2rem', display: '-webkit-box', WebkitLineClamp: 1, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                {item.analogySummary}
              </p>
              <div style={{ display: 'flex', gap: '0.85rem', marginTop: '0.35rem', fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                <span style={{ display: 'inline-flex', alignItems: 'center', gap: '0.2rem' }}>
                  <Clock size={12} /> {new Date(item.createdAt).toLocaleDateString()}
                </span>
                <span>{item.modelUsed}</span>
              </div>
            </div>

            <ChevronRight size={16} color="var(--text-muted)" style={{ flexShrink: 0 }} />
          </div>
        ))}
      </div>
    </section>
  );
};

