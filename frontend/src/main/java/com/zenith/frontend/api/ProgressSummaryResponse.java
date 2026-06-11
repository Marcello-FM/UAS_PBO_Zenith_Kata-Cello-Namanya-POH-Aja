package com.zenith.frontend.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgressSummaryResponse {

    private int totalAssessments;
    private double averageScore;
    private String averageMood;
    private int dayStreak;
    private List<AssessmentResponse> history = new ArrayList<>();

    public int getTotalAssessments() {
        return totalAssessments;
    }

    public void setTotalAssessments(int totalAssessments) {
        this.totalAssessments = totalAssessments;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public String getAverageMood() {
        return averageMood;
    }

    public void setAverageMood(String averageMood) {
        this.averageMood = averageMood;
    }

    public int getDayStreak() {
        return dayStreak;
    }

    public void setDayStreak(int dayStreak) {
        this.dayStreak = dayStreak;
    }

    public List<AssessmentResponse> getHistory() {
        return history;
    }

    public void setHistory(List<AssessmentResponse> history) {
        this.history = history;
    }
}
