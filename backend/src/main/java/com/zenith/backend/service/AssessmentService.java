package com.zenith.backend.service;

import com.zenith.backend.dto.AssessmentRequest;
import com.zenith.backend.dto.AssessmentResponse;
import com.zenith.backend.dto.ProgressSummaryResponse;
import com.zenith.backend.entity.StressAssessment;
import com.zenith.backend.entity.StressLevel;
import com.zenith.backend.entity.User;
import com.zenith.backend.exception.ApiException;
import com.zenith.backend.repository.StressAssessmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final StressAssessmentRepository assessmentRepository;

    public AssessmentService(StressAssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional
    public AssessmentResponse saveAssessment(User user, AssessmentRequest request) {
        validateAnswers(request.getAnswers());

        int score = request.getScore();
        if (score <= 0) {
            score = request.getAnswers().stream()
                    .mapToInt(answer -> answer + 1)
                    .sum();
        }

        StressLevel stressLevel = StressCalculator.resolveLevel(score);

        StressAssessment assessment = new StressAssessment();
        assessment.setUser(user);
        assessment.setScore(score);
        assessment.setStressLevel(stressLevel);
        assessment.setAnswers(toJson(request.getAnswers()));

        StressAssessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProgressSummaryResponse getProgress(User user) {
        List<StressAssessment> assessments = assessmentRepository.findByUserOrderByCreatedAtDesc(user);
        List<AssessmentResponse> history = assessments.stream().map(this::toResponse).toList();

        int total = assessments.size();
        double average = total == 0
                ? 0
                : assessments.stream().mapToInt(StressAssessment::getScore).average().orElse(0);

        return new ProgressSummaryResponse(
                total,
                Math.round(average * 10.0) / 10.0,
                StressCalculator.toMoodLabel(average),
                calculateDayStreak(assessments),
                history
        );
    }

    private void validateAnswers(List<Integer> answers) {
        if (answers == null || answers.size() != 5) {
            throw new ApiException("Jawaban harus berisi 5 item.", HttpStatus.BAD_REQUEST);
        }

        for (Integer answer : answers) {
            if (answer == null || answer < 0 || answer > 4) {
                throw new ApiException("Setiap jawaban harus antara 0 dan 4.", HttpStatus.BAD_REQUEST);
            }
        }
    }

    private String toJson(List<Integer> answers) {
        return answers.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private AssessmentResponse toResponse(StressAssessment assessment) {
        return new AssessmentResponse(
                assessment.getId(),
                assessment.getScore(),
                assessment.getStressLevel(),
                StressCalculator.toLabel(assessment.getStressLevel()),
                assessment.getCreatedAt()
        );
    }

    private int calculateDayStreak(List<StressAssessment> assessments) {
        if (assessments.isEmpty()) {
            return 0;
        }

        Set<LocalDate> activeDays = new HashSet<>();
        for (StressAssessment assessment : assessments) {
            LocalDateTime createdAt = assessment.getCreatedAt();
            if (createdAt != null) {
                activeDays.add(createdAt.toLocalDate());
            }
        }

        int streak = 0;
        LocalDate day = LocalDate.now();
        while (activeDays.contains(day)) {
            streak++;
            day = day.minusDays(1);
        }
        return streak;
    }
}
