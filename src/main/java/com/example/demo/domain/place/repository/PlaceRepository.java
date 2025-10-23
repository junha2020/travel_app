package com.example.demo.domain.place.repository;

import com.example.demo.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // 특정 여행 계획에 속한 장소 모두 찾기
    List<Place> findByTravelPlan_Id(Long planId);

    // 특정 여행 계획의 특정 일자에 속한 장소 모두 찾기
    List<Place> findByTravelPlan_IdAndDay(Long planId, int day);

    List<Place> findByNameContainingIgnoreCase(String name);
}
