package com.zenith.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProgressSummaryResponse {

    private int totalAssessments;
    private double averageScore;
    private String averageMood;
    private int dayStreak;
    private List<AssessmentResponse> history;
}
