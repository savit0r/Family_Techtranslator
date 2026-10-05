import React from 'react';
import { Plus, Trash2, Check } from 'lucide-react';
import type { FamilyMember } from '../types';

interface ProfileSelectorProps {
  members: FamilyMember[];
  selectedId: number | null;
  onSelect: (id: number) => void;
  onOpenCreateModal: () => void;
  onDeleteMember: (id: number) => void;
}

export const ProfileSelector: React.FC<ProfileSelectorProps> = ({
  members,
  selectedId,
  onSelect,
  onOpenCreateModal,
  onDeleteMember,
}) => {
  return (
    <section style={{ marginBottom: '2.5rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem', flexWrap: 'wrap', gap: '0.75rem' }}>
        <div>
          <h2 style={{ fontSize: '1.15rem', fontWeight: 700, color: 'var(--text-primary)', letterSpacing: '-0.01em' }}>
            Who are you talking to?
          </h2>
          <p style={{ fontSize: '0.88rem', color: 'var(--text-secondary)' }}>
            Pick someone so the explanation can speak their language.
          </p>
        </div>

        <button className="btn-secondary" onClick={onOpenCreateModal}>
          <Plus size={15} />
          Add Profile
        </button>
      </div>

      {members.length === 0 ? (
        <div className="card-surface" style={{
          padding: '2.5rem 1.5rem',
          textAlign: 'center',
          borderStyle: 'dashed'
        }}>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.92rem', marginBottom: '1rem' }}>
            Pick someone from your family, then give me a technical concept to explain.
          </p>
          <button className="btn-primary" onClick={onOpenCreateModal}>
            <Plus size={16} />
            Create First Profile
          </button>
        </div>
      ) : (
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))',
          gap: '0.85rem'
        }}>
          {members.map((member) => {
            const isSelected = selectedId === member.id;
            const initial = member.name ? member.name.charAt(0).toUpperCase() : '?';

            return (
              <div
                key={member.id}
                onClick={() => onSelect(member.id)}
                role="button"
                tabIndex={0}
                aria-pressed={isSelected}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' || e.key === ' ') {
                    onSelect(member.id);
                  }
                }}
                className={`profile-card ${isSelected ? 'selected' : ''}`}
              >
                <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: '0.75rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                    <div style={{
                      width: '40px',
                      height: '40px',
                      borderRadius: '50%',
                      backgroundColor: isSelected ? 'var(--accent-primary)' : 'var(--bg-surface-subtle)',
                      color: isSelected ? 'var(--text-on-accent)' : 'var(--text-secondary)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontWeight: 600,
                      fontSize: '1rem',
                      flexShrink: 0,
                      transition: 'all 0.2s ease'
                    }}>
                      {initial}
                    </div>

                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                        <h3 style={{ fontSize: '0.98rem', fontWeight: 700, color: 'var(--text-primary)', lineHeight: 1.3 }}>
                          {member.name}
                        </h3>
                        {isSelected && (
                          <span style={{
                            width: '16px',
                            height: '16px',
                            borderRadius: '50%',
                            backgroundColor: 'var(--accent-primary)',
                            display: 'inline-flex',
                            alignItems: 'center',
                            justifyContent: 'center'
                          }}>
                            <Check size={11} color="#ffffff" />
                          </span>
                        )}
                      </div>
                      <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)', marginTop: '0.1rem' }}>
                        {member.relationship} · {member.occupation}
                      </p>
                    </div>
                  </div>

                  <button
                    aria-label={`Delete ${member.name}`}
                    onClick={(e) => {
                      e.stopPropagation();
                      onDeleteMember(member.id);
                    }}
                    style={{
                      background: 'transparent',
                      border: 'none',
                      color: 'var(--text-tertiary)',
                      cursor: 'pointer',
                      padding: '0.2rem',
                      borderRadius: 'var(--radius-sm)',
                      transition: 'color 0.15s'
                    }}
                    onMouseEnter={(e) => (e.currentTarget.style.color = 'var(--accent-error)')}
                    onMouseLeave={(e) => (e.currentTarget.style.color = 'var(--text-tertiary)')}
                  >
                    <Trash2 size={14} />
                  </button>
                </div>

                <div style={{ marginTop: '0.65rem', paddingTop: '0.5rem', borderTop: '1px solid var(--border-subtle)', fontSize: '0.78rem', color: 'var(--text-tertiary)', fontStyle: 'italic' }}>
                  "{member.familiarTopics || member.interests}"
                </div>
              </div>
            );
          })}
        </div>
      )}
    </section>
  );
};

