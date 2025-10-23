package com.example.demo.domain.placePlan.repository;

import com.example.demo.domain.placePlan.entitiy.PlanPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {

    List<PlanPlace> findByTravelPlan_Id(Long planId);

    List<PlanPlace> findByTravelPlan_IdAndDayOrderByDayAsc(Long planId, int day);
}
