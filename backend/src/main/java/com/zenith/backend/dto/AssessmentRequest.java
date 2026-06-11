package com.zenith.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssessmentRequest {

    private int score;

    @NotNull
    @Size(min = 5, max = 5)
    private List<Integer> answers;
}
