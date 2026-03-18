# ResumeAI Optimizer

AI-powered resume analyzer & optimizer — upload your CV, get ATS score, strengths/weaknesses, suggestions, and a rewritten version optimized for modern software roles.

Built for job seekers who want to stand out in 2026.

[Live Demo (Frontend) →](https://resumeai-optimizer-five.vercel.app/)  
[Backend API](https://resumeai-backend-latest.onrender.com/api/resume/health)

---

## Features

- Upload PDF resume or paste text
- Optional job description input for better tailoring
- Instant AI analysis using Groq (fast & powerful LLM)
- ATS score (0–100)
- Strengths & weaknesses lists
- Concrete improvement suggestions
- Full rewritten resume (ATS-friendly, keyword-optimized)
- Beautiful, modern UI with dark/light mode & bilingual support (EN/KA)
- Responsive design (mobile + desktop)

## Tech Stack

**Frontend**
- React + Vite (fast dev & build)
- Tailwind CSS v4 (utility-first styling)
- Axios (API calls)
- react-i18next (bilingual support)

**Backend**
- Java 21 + Spring Boot 4.0.3
- Spring AI (OpenAI-compatible client for Groq)
- Apache PDFBox (PDF text extraction)
- Maven (build tool)

**AI**
- Groq API (llama-3.3-70b-versatile model — fast & cost-effective)

**Deployment**
- Frontend: Vercel
- Backend: Render (Dockerized)

## Screenshots

.........................................................................................

## Live Demo

- Frontend (hosted on Vercel): https://resumeai-optimizer-five.vercel.app/
- Backend health check: https://resumeai-backend-latest.onrender.com/api/resume/health

## Local Development

### Prerequisites
- Java 21 (Temurin / Eclipse Adoptium recommended)
- Node.js 18+ & npm
- Docker Desktop (optional — only if you want to test container locally)
- Groq API key (get free credits at https://console.groq.com/keys)

