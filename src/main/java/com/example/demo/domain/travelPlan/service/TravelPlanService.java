package com.example.demo.domain.travelPlan.service;

import com.example.demo.domain.travelPlan.dto.TravelPlanRequestDTO;
import com.example.demo.domain.travelPlan.dto.TravelPlanResponseDTO;

import java.util.List;

public interface TravelPlanService {

    public TravelPlanResponseDTO create(TravelPlanRequestDTO travelPlanRequestDTO);
    public TravelPlanResponseDTO update(Long Id, TravelPlanRequestDTO travelPlanRequestDTO);
    public void delete(Long Id);
    public TravelPlanResponseDTO findById(Long Id);
    public List<TravelPlanResponseDTO> findByUserId(Long userId);

}
