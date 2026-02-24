package com.nrs1209.travelapp.domain.travelPlan.repository;

import com.nrs1209.travelapp.domain.travelPlan.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {
    List<TravelPlan> findByUserId(Long userId);

    List<TravelPlan> findByTitle(String title);

    List<TravelPlan> findByIsPublicTrue();
}
