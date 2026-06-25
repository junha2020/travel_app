package com.nrs1209.travelapp.plan.repository;

import com.nrs1209.travelapp.plan.entity.PlanPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {

    List<PlanPlace> findByTravelPlan_Id(Long planId);

    List<PlanPlace> findByTravelPlan_IdAndDayOrderByDayAsc(Long planId, int day);
}
