import React, { useState } from 'react';
import { X } from 'lucide-react';
import type { FamilyMemberRequest, TechLevel } from '../types';

interface ProfileModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: FamilyMemberRequest) => Promise<void>;
}

export const ProfileModal: React.FC<ProfileModalProps> = ({ isOpen, onClose, onSubmit }) => {
  const [formData, setFormData] = useState<FamilyMemberRequest>({
    name: '',
    relationship: '',
    occupation: '',
    interests: '',
    familiarTopics: '',
    techLevel: 'BEGINNER',
    preferredLanguage: 'English',
    communicationStyle: 'Storytelling & Visual',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!formData.name.trim() || !formData.relationship.trim() || !formData.occupation.trim()) {
      setError('Please fill out all required fields.');
      return;
    }

    try {
      setLoading(true);
      await onSubmit(formData);
      setFormData({
        name: '',
        relationship: '',
        occupation: '',
        interests: '',
        familiarTopics: '',
        techLevel: 'BEGINNER',
        preferredLanguage: 'English',
        communicationStyle: 'Storytelling & Visual',
      });
      onClose();
    } catch (err: any) {
      setError(err.message || 'Failed to save family member profile.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      position: 'fixed',
      top: 0, left: 0, right: 0, bottom: 0,
      background: 'rgba(28, 25, 23, 0.4)',
      backdropFilter: 'blur(4px)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 1000,
      padding: '1rem'
    }}>
      <div className="card" style={{
        width: '100%',
        maxWidth: '540px',
        padding: '1.75rem',
        boxShadow: 'var(--shadow-md)'
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem', paddingBottom: '0.75rem', borderBottom: '1px solid var(--border-color)' }}>
          <div>
            <h2 style={{ fontSize: '1.15rem', fontWeight: 600, color: 'var(--text-primary)' }}>Add Family Member</h2>
            <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>Help the translator understand how they relate to concepts</p>
          </div>
          <button
            aria-label="Close dialog"
            onClick={onClose}
            style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer', padding: '0.25rem' }}
          >
            <X size={18} />
          </button>
        </div>

        {error && (
          <div className="error-alert" style={{ marginBottom: '1rem' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
                Name *
              </label>
              <input
                type="text"
                className="input-field"
                placeholder="e.g. Maria"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
                Relationship *
              </label>
              <input
                type="text"
                className="input-field"
                placeholder="e.g. Grandmother"
                value={formData.relationship}
                onChange={(e) => setFormData({ ...formData, relationship: e.target.value })}
                required
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
                Occupation *
              </label>
              <input
                type="text"
                className="input-field"
                placeholder="e.g. Master Gardener"
                value={formData.occupation}
                onChange={(e) => setFormData({ ...formData, occupation: e.target.value })}
                required
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
                Technical Familiarity *
              </label>
              <select
                className="input-field"
                value={formData.techLevel}
                onChange={(e) => setFormData({ ...formData, techLevel: e.target.value as TechLevel })}
              >
                <option value="BEGINNER">Non-technical (Beginner)</option>
                <option value="INTERMEDIATE">Uses basic tools (Intermediate)</option>
                <option value="ADVANCED">Tech-savvy (Advanced)</option>
              </select>
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
              Hobbies & Interests *
            </label>
            <input
              type="text"
              className="input-field"
              placeholder="e.g. Gardening, sourdough baking, woodworking"
              value={formData.interests}
              onChange={(e) => setFormData({ ...formData, interests: e.target.value })}
              required
            />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
              Metaphor Domains / Familiar Topics *
            </label>
            <input
              type="text"
              className="input-field"
              placeholder="e.g. Soil nutrients, greenhouse climate, recipes"
              value={formData.familiarTopics}
              onChange={(e) => setFormData({ ...formData, familiarTopics: e.target.value })}
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
                Preferred Language *
              </label>
              <input
                type="text"
                className="input-field"
                placeholder="e.g. English"
                value={formData.preferredLanguage}
                onChange={(e) => setFormData({ ...formData, preferredLanguage: e.target.value })}
                required
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)', marginBottom: '0.35rem' }}>
                Communication Style *
              </label>
              <input
                type="text"
                className="input-field"
                placeholder="e.g. Storytelling & Visual"
                value={formData.communicationStyle}
                onChange={(e) => setFormData({ ...formData, communicationStyle: e.target.value })}
                required
              />
            </div>
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '1rem', paddingTop: '0.5rem' }}>
            <button type="button" className="btn-secondary" onClick={onClose} disabled={loading}>
              Cancel
            </button>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Saving...' : 'Save Profile'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

