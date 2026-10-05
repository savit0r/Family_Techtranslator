# FamilyTech Translator 🌉

> *"Finally, a way to explain what you actually do."*

**FamilyTech Translator** is a human-centered application designed to help software engineers explain complex technical concepts (like *Kubernetes*, *Database Indexing*, *REST APIs*, or *Recursion*) to their family members. 

Instead of generic jargon, it translates concepts into relatable, personalized analogies drawn directly from the family member's real-world background, hobbies, and occupation.

---

## ✨ Features

- 👨‍👩‍👧 **Personalized Family Profiles**: Define family members by relationship, occupation, hobbies, and metaphor domains (e.g., tailoring, gardening, cooking).
- 💡 **Tailored Analogies & Step-by-Step Breakdown**: Generates intuitive analogies, step-by-step breakdowns, and technical mappings.
- 🔄 **Explain-Back Educational Learning Loop**: Ask family members to explain the concept back in their own words; the AI evaluates what they understood, identifies gaps, and provides short clarifications.
- 🦙 **Dual LLM Provider Architecture**:
  - **Local Ollama Integration**: Uses `llama3.2` locally for private, zero-latency inference.
  - **Dev Mode / Mock LLM**: Automatically falls back to mock responses when Ollama is offline for frictionless local development.
- 🎨 **Warm, Refined UI/UX**:
  - Light & Dark mode support with smooth transitions.
  - Mobile-responsive layout.
  - Micro-interactions, custom design tokens, and clear editorial typography.

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Java 25 / Spring Boot 3.3.4
- **Testing**: JUnit 5, Spring Boot Test, Mockito (30/30 passing unit & integration tests)
- **Architecture**: REST API (`/api/v1/translate`, `/api/v1/explain-back`, `/api/v1/family-members`, `/api/v1/status`)

### Frontend
- **Framework**: React 19 + TypeScript + Vite
- **Styling**: Vanilla CSS Design System with theme variables (Light/Dark mode)
- **Icons**: Lucide React

---

## 🚀 Quickstart Guide

### Prerequisites
- **Java**: JDK 21 or higher
- **Node.js**: v18+ and `npm`
- **Ollama** *(Optional)*: Installed and running locally with `llama3.2` model (`ollama run llama3.2`)

---

### 1. Start the Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

The Spring Boot backend will start on **`http://localhost:8080`**.

> **Note**: If Ollama is running on `http://localhost:11434`, the server will automatically connect to `llama3.2`. Otherwise, it seamlessly falls back to Dev Mode (Mock LLM).

---

### 2. Start the Frontend (React + Vite)

In a new terminal window:

```bash
cd frontend
npm install
npm run dev
```

Open your browser and navigate to **`http://localhost:5173`**.

---

## 🧪 Running Tests & Validation

### Run Backend Tests

```bash
cd backend
mvn test
```

### Build & Verify Frontend

```bash
cd frontend
npm run build
```

---

## 📂 Project Structure

```
Family Techtranslator/
├── backend/                  # Spring Boot backend application
│   ├── src/main/java/        # Controllers, Services, DTOs, AI Providers
│   └── src/test/java/        # Unit & Integration test suite
├── frontend/                 # React TypeScript frontend
│   ├── src/
│   │   ├── components/       # Header, ProfileSelector, TranslatorForm, ExplanationView
│   │   ├── api.ts            # Frontend API client
│   │   ├── index.css         # Theme design tokens & CSS rules
│   │   └── App.tsx           # Main application wrapper & theme state
└── README.md
```

---

## 📄 License

MIT License. Built with care for family conversations.
