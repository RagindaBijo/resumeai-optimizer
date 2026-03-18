package com.luka.resumeai_optimizer.service;

import com.luka.resumeai_optimizer.dto.AnalyzeRequest;
import com.luka.resumeai_optimizer.dto.AnalyzeResponse;
import com.luka.resumeai_optimizer.util.PdfTextExtractor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {

    private final PdfTextExtractor pdfExtractor;
    private final ChatClient chatClient;

    @Autowired
    public ResumeService(PdfTextExtractor pdfExtractor, ChatClient.Builder chatClientBuilder) {
        this.pdfExtractor = pdfExtractor;
        this.chatClient = chatClientBuilder.build();
    }

    public AnalyzeResponse analyzeResume(AnalyzeRequest request) throws IOException {
        // 1. Extract resume text
        String resumeText;
        if (request.getResumeFile() != null && !request.getResumeFile().isEmpty()) {
            resumeText = pdfExtractor.extractTextFromPdf(request.getResumeFile());
        } else if (request.getResumeText() != null && !request.getResumeText().isBlank()) {
            resumeText = request.getResumeText().trim();
        } else {
            throw new IllegalArgumentException("No resume provided: either upload a PDF file or paste resume text");
        }

        // 2. Prepare prompt
        String systemPrompt = getResumeAnalysisSystemPrompt();
        String userInput = buildUserInput(resumeText, request.getJobDescription());

        Prompt prompt = new PromptTemplate(systemPrompt + "\n\n" + userInput).create();

        // 3. Call AI and get response text
        String aiOutput;
        try {
            aiOutput = chatClient.prompt(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            AnalyzeResponse errorResponse = new AnalyzeResponse();
            errorResponse.setSummary("Failed to get response from AI: " + e.getMessage());
            errorResponse.setAtsScore(0);   
            return errorResponse;
        }

        // 4. Parse the structured markdown output
        return parseAiOutput(aiOutput);
    }

    private String getResumeAnalysisSystemPrompt() {
        return """
        You are an expert ATS-optimized resume writer, career coach, and technical hiring specialist with 20+ years of experience helping software developers, IT professionals, and career changers land jobs at top tech companies.

        IMPORTANT RULES YOU MUST FOLLOW STRICTLY:
        1. First, automatically detect the main language of the resume text (usually English or Georgian).
        2. Write your ENTIRE response in the SAME language as the resume:
           - If the resume is primarily in Georgian → respond fully in Georgian
           - If the resume is primarily in English → respond fully in English
           - Never mix languages. Never default to English if the resume is Georgian.
        3. Be extremely detailed, thorough, and professional in your analysis. Provide long, high-value feedback — aim for depth, not brevity.
        4. Return your response in this **exact markdown format** — do not add any introductory text, explanations, or extra content outside these sections:

        ## ATS Score
        <integer between 0 and 100> — give a realistic, well-justified score

        ## Strengths
        - Very detailed bullet point 1 (explain why it's a strength, give examples from the resume)
        - Very detailed bullet point 2 (include specific achievements or wording that works well)
        - Very detailed bullet point 3 (and more — aim for 4–8 strong points if possible)
        ...

        ## Weaknesses
        - Very detailed bullet point 1 (explain exactly why this hurts ATS or recruiter perception)
        - Very detailed bullet point 2 (point out missing keywords, vague language, poor formatting, etc.)
        - Very detailed bullet point 3 (and more — be honest and specific)
        ...

        ## Suggestions & Improvements
        - Very detailed, actionable suggestion 1 (explain what to change, why, and give example phrasing)
        - Very detailed, actionable suggestion 2 (suggest keywords to add, metrics to quantify, structure improvements)
        - Very detailed, actionable suggestion 3 (and more — give 5–10 concrete recommendations if possible)
        ...

        ## Improved Resume
        Full rewritten version of the resume in the SAME language as the original.
        - Keep the original structure and sections as much as possible
        - Optimize heavily for ATS: plain text friendly, standard headings, keywords from job description
        - Quantify achievements wherever possible (numbers, percentages, impact)
        - Use strong action verbs
        - Make it longer and more detailed if the original is too short — aim for a professional, impactful resume
        - Do not summarize — provide the complete rewritten text ready to copy-paste

        ## Summary
        A detailed paragraph (5–10 sentences) summarizing the overall quality of the resume, its strongest and weakest areas, how well it matches modern tech roles (Java, Spring Boot, React, Kotlin, mobile, AI integration, etc.), and the most important next steps for the candidate.

        Focus on:
        - ATS compatibility (keywords, no tables/graphics, clear sections)
        - Quantifiable achievements (numbers, impact, results)
        - Relevance to current tech job market (2025–2026 trends)
        - Professional tone, strong action verbs, clarity, conciseness with depth
        - Career story and progression (if visible)

        Always use proper bullet points starting with "- ".
        Be generous with details — the candidate wants deep, valuable feedback.
        """;
    }

    private String buildUserInput(String resumeText, String jobDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUME TEXT ===\n");
        sb.append(resumeText).append("\n\n");

        if (jobDescription != null && !jobDescription.trim().isEmpty()) {
            sb.append("=== TARGET JOB DESCRIPTION ===\n");
            sb.append(jobDescription.trim()).append("\n\n");
        }

        sb.append("Analyze and optimize this resume for modern software development roles.");
        return sb.toString();
    }

    private AnalyzeResponse parseAiOutput(String aiOutput) {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setImprovedResumeText(aiOutput); // fallback

        if (aiOutput == null || aiOutput.trim().isEmpty()) {
            response.setSummary("No AI response received.");
            response.setAtsScore(0);
            return response;
        }

        // Normalize: replace multiple newlines with single, trim
        String normalized = aiOutput.replaceAll("\\n\\s*\\n+", "\n").trim();

        // Split on "## " but keep the delimiter
        String[] sections = normalized.split("(?=## )");

        String currentSection = "";
        for (String part : sections) {
            part = part.trim();
            if (part.isEmpty()) continue;

            if (part.startsWith("## ")) {
                // New section starts
                processSection(currentSection, response);
                currentSection = part;
            } else if (!currentSection.isEmpty()) {
                // Continuation of previous section
                currentSection += "\n" + part;
            } else {
                currentSection = part;
            }
        }
        // Don't forget the last section
        processSection(currentSection, response);

        // Fallbacks
        if (response.getAtsScore() == 0) response.setAtsScore(65);
        if (response.getSummary() == null || response.getSummary().isBlank()) {
            response.setSummary("AI analysis completed — check improved resume.");
        }

        return response;
    }

    private void processSection(String section, AnalyzeResponse response) {
        if (section.trim().isEmpty() || !section.startsWith("## ")) return;

        String content = section.substring(3).trim(); // remove "## "
        int nlIndex = content.indexOf('\n');
        String title = (nlIndex >= 0 ? content.substring(0, nlIndex) : content).trim();
        String body = (nlIndex >= 0 ? content.substring(nlIndex + 1).trim() : "");

        switch (title.trim()) {
            case "ATS Score":
                try {
                    String firstLine = body.split("\n")[0].trim();
                    int score = Integer.parseInt(firstLine.replaceAll("[^0-9]", ""));
                    response.setAtsScore(Math.max(0, Math.min(100, score)));
                } catch (Exception ignored) {}
                break;

            case "Strengths":
                response.setStrengths(extractBulletList(body));
                break;

            case "Weaknesses":
                response.setWeaknesses(extractBulletList(body));
                break;

            case "Suggestions & Improvements":
                response.setSuggestions(extractBulletList(body));
                break;

            case "Improved Resume":
                response.setImprovedResumeText(body.trim());
                break;

            case "Summary":
                response.setSummary(body.trim());
                break;
        }
    }
    /**
     * Converts markdown bullet list to clean List<String>
     * Handles both "- " and "* " prefixes
     */
    private List<String> extractBulletList(String content) {
        if (content == null || content.trim().isEmpty()) {
            return List.of();
        }

        List<String> bullets = new ArrayList<>();
        String[] lines = content.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("- ") || line.startsWith("* ")) {
                String item = line.substring(2).trim();
                if (!item.isEmpty()) {
                    bullets.add(item);
                }
            }
        }

        return bullets;
    }
}