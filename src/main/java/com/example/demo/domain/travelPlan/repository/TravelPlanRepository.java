package com.example.demo.domain.travelPlan.repository;

import com.example.demo.domain.travelPlan.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {
    List<TravelPlan> findByUserId(Long userId);

    List<TravelPlan> findByTitle(String title);

    List<TravelPlan> findByIsPublicTrue();
}
