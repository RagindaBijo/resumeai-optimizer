package com.luka.resumeai_optimizer.controller;

import com.luka.resumeai_optimizer.dto.AnalyzeRequest;
import com.luka.resumeai_optimizer.dto.AnalyzeResponse;
import com.luka.resumeai_optimizer.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = {
        "https://resumeai-optimizer-five.vercel.app",
        "http://localhost:5173",           // your local Vite dev server
        "http://localhost:5174"            // sometimes Vite uses next port
})  // For dev only — allow frontend calls (later restrict to your Vercel domain)
public class ResumeController {

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnalyzeResponse> analyzeResume(
            @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestPart(value = "resumeText", required = false) String resumeText,
            @RequestPart(value = "jobDescription", required = false) String jobDescription) {

        try {
            AnalyzeRequest request = new AnalyzeRequest();
            request.setResumeFile(resumeFile);
            request.setResumeText(resumeText);
            request.setJobDescription(jobDescription);

            AnalyzeResponse response = resumeService.analyzeResume(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            AnalyzeResponse error = new AnalyzeResponse();
            error.setSummary("Invalid input: " + e.getMessage());
            error.setAtsScore(0);
            return ResponseEntity.badRequest().body(error);
        } catch (IOException e) {
            AnalyzeResponse error = new AnalyzeResponse();
            error.setSummary("Error processing PDF: " + e.getMessage());
            error.setAtsScore(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            AnalyzeResponse error = new AnalyzeResponse();
            error.setSummary("Unexpected error: " + e.getMessage());
            error.setAtsScore(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Simple health check endpoint
    @GetMapping("/health")
    public String healthCheck() {
        return "ResumeAI Optimizer backend is running!";
    }
}