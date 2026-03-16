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
            You are an expert ATS-optimized resume writer and career coach with 15+ years of experience helping software developers and IT professionals land jobs.
            
            Analyze the provided resume text (and optional job description) carefully.
            Focus on:
            - ATS compatibility (keywords, structure, no fancy formatting)
            - Quantifiable achievements
            - Relevance to modern tech roles (Java, Spring Boot, React, mobile, AI integration)
            - Clarity, conciseness, strong action verbs
            
            Return your entire response in this **exact markdown format** — do not add any extra text, explanations, or comments outside the sections:
            
            ## ATS Score
            <integer between 0 and 100>
            
            ## Strengths
            - bullet point 1
            - bullet point 2
            ...
            
            ## Weaknesses
            - bullet point 1
            - bullet point 2
            ...
            
            ## Suggestions & Improvements
            - concrete suggestion 1
            - concrete suggestion 2
            ...
            
            ## Improved Resume
            Paste the full rewritten resume here. Keep similar structure to the original but:
            - Optimize for ATS (plain text friendly)
            - Add relevant keywords from job description if provided
            - Quantify achievements where possible
            - Improve wording and impact
            
            ## Summary
            One short paragraph (3-5 sentences) summarizing the key findings and overall quality.
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