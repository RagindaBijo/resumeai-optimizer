package com.luka.resumeai_optimizer.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AnalyzeRequest {
    private MultipartFile resumeFile;       // PDF upload (main way)
    private String resumeText;              // fallback: paste raw text
    private String jobDescription;          // optional: paste job desc for better tailoring
}