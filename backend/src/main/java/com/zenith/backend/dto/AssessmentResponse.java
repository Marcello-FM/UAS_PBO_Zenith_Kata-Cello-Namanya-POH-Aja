package com.zenith.backend.dto;

import com.zenith.backend.entity.StressLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AssessmentResponse {

    private Long id;
    private int score;
    private StressLevel stressLevel;
    private String stressLabel;
    private LocalDateTime createdAt;
}
