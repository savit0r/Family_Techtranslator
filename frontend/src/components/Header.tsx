import React from 'react';
import { Sun, Moon } from 'lucide-react';
import type { SystemStatus } from '../types';

interface HeaderProps {
  status: SystemStatus | null;
  theme: 'light' | 'dark';
  onToggleTheme: () => void;
}

export const Header: React.FC<HeaderProps> = ({ status, theme, onToggleTheme }) => {
  return (
    <header style={{ marginBottom: '2.5rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem' }}>
        <div style={{ maxWidth: '640px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '0.35rem' }}>
            <h1 style={{ fontSize: '1.65rem', fontWeight: 700, color: 'var(--text-primary)', letterSpacing: '-0.025em' }}>
              FamilyTech Translator
            </h1>
          </div>

          <p style={{ color: 'var(--text-primary)', fontSize: '1.1rem', fontWeight: 600, letterSpacing: '-0.015em', marginBottom: '0.25rem' }}>
            "Finally, a way to explain what you actually do."
          </p>

          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', lineHeight: 1.5 }}>
            Turn technical concepts into explanations your family can understand — using their world, not yours.
          </p>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          {/* Theme Toggle Button */}
          <button
            onClick={onToggleTheme}
            aria-label={theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'}
            className="btn-secondary"
            style={{
              padding: '0.45rem',
              borderRadius: 'var(--radius-full)',
              width: '36px',
              height: '36px',
              display: 'inline-flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}
            title={theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'}
          >
            {theme === 'dark' ? (
              <Sun size={17} color="var(--accent-warning)" />
            ) : (
              <Moon size={17} color="var(--text-secondary)" />
            )}
          </button>

          {/* AI Environment Status Badge */}
          <div style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: '0.4rem',
            padding: '0.35rem 0.75rem',
            borderRadius: 'var(--radius-full)',
            background: 'var(--bg-surface)',
            border: '1px solid var(--border-subtle)',
            fontSize: '0.78rem',
            color: 'var(--text-secondary)',
            boxShadow: 'var(--shadow-sm)'
          }}>
            <span style={{
              width: '6px',
              height: '6px',
              borderRadius: '50%',
              background: status?.ollamaAvailable ? 'var(--accent-success)' : 'var(--accent-warning)',
              display: 'inline-block'
            }} />
            {status ? (
              status.ollamaAvailable ? (
                <span style={{ color: 'var(--text-primary)', fontWeight: 500 }}>
                  Ollama Local (llama3.2)
                </span>
              ) : (
                <span style={{ color: 'var(--text-secondary)', fontWeight: 500 }}>
                  Dev Mode (Mock LLM)
                </span>
              )
            ) : (
              <span>Connecting...</span>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

