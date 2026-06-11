package com.zenith.backend.service;

import com.zenith.backend.entity.StressLevel;

public final class StressCalculator {

    private StressCalculator() {}

    public static StressLevel resolveLevel(int score) {
        if (score > 20) {
            return StressLevel.VERY_STRESS;
        }
        if (score >= 15) {
            return StressLevel.MODERATE;
        }
        return StressLevel.LOW;
    }

    public static String toLabel(StressLevel level) {
        return switch (level) {
            case LOW -> "Low Stress";
            case MODERATE -> "Moderate Stress";
            case VERY_STRESS -> "Very Stress";
        };
    }

    public static String toMoodLabel(double averageScore) {
        if (averageScore > 20) {
            return "Stressed";
        }
        if (averageScore >= 15) {
            return "Neutral";
        }
        return "Happy";
    }
}
