package com.luka.resumeai_optimizer.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnalyzeResponse {
    private int atsScore;                   // 0-100
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> suggestions;
    private String improvedResumeText;      // full rewritten version
    private String summary;                 // short overview
}