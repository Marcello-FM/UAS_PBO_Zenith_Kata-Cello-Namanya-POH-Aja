package com.zenith.backend.repository;

import com.zenith.backend.entity.StressAssessment;
import com.zenith.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StressAssessmentRepository extends JpaRepository<StressAssessment, Long> {

    List<StressAssessment> findByUserOrderByCreatedAtDesc(User user);
}
