package com.luka.resumeai_optimizer;

import com.luka.resumeai_optimizer.dto.AnalyzeRequest;
import com.luka.resumeai_optimizer.dto.AnalyzeResponse;
import com.luka.resumeai_optimizer.service.ResumeService;
import com.luka.resumeai_optimizer.util.PdfTextExtractor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class AiSmokeTest implements CommandLineRunner {

    private final ChatClient chatClient;
    private final PdfTextExtractor pdfExtractor;
    private final ResumeService resumeService;

    @Autowired
    public AiSmokeTest(
            ChatClient.Builder chatClientBuilder,
            PdfTextExtractor pdfExtractor,
            ResumeService resumeService) {
        this.chatClient = chatClientBuilder.build();
        this.pdfExtractor = pdfExtractor;
        this.resumeService = resumeService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== AiSmokeTest STARTED ===\n");

        // Test 1: Simple Groq AI call (should still work)
        try {
            String joke = chatClient.prompt()
                    .user("Tell me a very short joke about Java programmers.")
                    .call()
                    .content();
            System.out.println("AI Joke: " + joke);
        } catch (Exception e) {
            System.err.println("AI Joke test failed: " + e.getMessage());
        }

        // Test 2: PDF extraction from your real file
        try {
            Path pdfPath = Path.of("D:\\Luka\\Resume.pdf");
            if (Files.exists(pdfPath)) {
                byte[] pdfBytes = Files.readAllBytes(pdfPath);
                String extractedText = pdfExtractor.extractTextFromBytes(pdfBytes);
                System.out.println("\nExtracted PDF text (first 300 chars):\n" +
                        extractedText.substring(0, Math.min(300, extractedText.length())) + "...");
            } else {
                System.out.println("\nPDF file not found at: D:\\Luka\\Resume.pdf");
            }
        } catch (Exception e) {
            System.err.println("PDF extraction test failed: " + e.getMessage());
        }

        // Test 3: End-to-end ResumeService test (using pasted text fallback)
        try {
            AnalyzeRequest request = new AnalyzeRequest();
            // For simplicity in smoke test, use pasted text instead of file
            // You can later test file upload via Postman/controller
            request.setResumeText("""
                    John Doe
                    Java Developer
                    Tbilisi, Georgia
                    +995 555 123 456 | john.doe@email.com
                    
                    Summary
                    Experienced Java developer with 3 years in Spring Boot and REST APIs.
                    
                    Experience
                    Backend Developer, Company X, 2022–present
                    - Developed REST services
                    - Used Spring Data JPA
                    
                    Skills
                    Java, Spring Boot, SQL, Git
                    """);

            // Optional: add job description
            // request.setJobDescription("Looking for Spring Boot developer with experience in microservices and React...");

            System.out.println("\nSending resume to AI analysis...");

            AnalyzeResponse response = resumeService.analyzeResume(request);

            System.out.println("\n=== AI Resume Analysis Result ===");
            System.out.println("ATS Score: " + response.getAtsScore());
            System.out.println("Summary: " + response.getSummary());
            System.out.println("\nImproved Resume (partial):\n" +
                    response.getImprovedResumeText().substring(0,
                            Math.min(400, response.getImprovedResumeText().length())) + "...");

            // Print other fields if you want
            // System.out.println("Strengths: " + response.getStrengths());
            // System.out.println("Weaknesses: " + response.getWeaknesses());

        } catch (Exception e) {
            System.err.println("Resume analysis test failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== AiSmokeTest FINISHED ===\n");
    }
}