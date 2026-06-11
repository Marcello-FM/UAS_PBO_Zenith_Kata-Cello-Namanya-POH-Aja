package com.zenith.backend.controller;

import com.zenith.backend.dto.AssessmentRequest;
import com.zenith.backend.dto.AssessmentResponse;
import com.zenith.backend.dto.AuthResponse;
import com.zenith.backend.dto.LoginRequest;
import com.zenith.backend.dto.ProgressSummaryResponse;
import com.zenith.backend.dto.RegisterRequest;
import com.zenith.backend.dto.ResetPasswordRequest;
import com.zenith.backend.entity.User;
import com.zenith.backend.exception.ApiException;
import com.zenith.backend.service.AssessmentService;
import com.zenith.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final AuthService authService;
    private final AssessmentService assessmentService;

    public ApiController(AuthService authService, AssessmentService assessmentService) {
        this.authService = authService;
        this.assessmentService = assessmentService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Password berhasil diperbarui."));
    }

    @PostMapping("/assessments")
    public ResponseEntity<AssessmentResponse> saveAssessment(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AssessmentRequest request) {
        User user = authService.getCurrentUser(requireUsername(userDetails));
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentService.saveAssessment(user, request));
    }

    @GetMapping("/assessments/progress")
    public ResponseEntity<ProgressSummaryResponse> getProgress(@AuthenticationPrincipal UserDetails userDetails) {
        User user = authService.getCurrentUser(requireUsername(userDetails));
        return ResponseEntity.ok(assessmentService.getProgress(user));
    }

    private String requireUsername(UserDetails userDetails) {
        if (userDetails == null) {
            throw new ApiException("Sesi tidak valid. Silakan login ulang.", HttpStatus.UNAUTHORIZED);
        }
        return userDetails.getUsername();
    }
}
