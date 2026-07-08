package com.nrs1209.travelapp.plan.repository;

import com.nrs1209.travelapp.plan.entity.PlanPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {

    List<PlanPlace> findByPlan_Id(Long planId);

    List<PlanPlace> findByPlan_IdAndDayOrderByDayAsc(Long planId, int day);

    // 특정 일정 ID에 딸린 모든 자식 장소들 전수 삭제하는 쿼리
    void deleteByPlan_Id(Long planId);
}
