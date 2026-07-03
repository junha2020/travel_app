package com.nrs1209.travelapp.plan.repository;

import com.nrs1209.travelapp.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByUserId(Long userId);

    List<Plan> findByTitle(String title);

    List<Plan> findByIsPublicTrue();
}
