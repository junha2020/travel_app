package com.example.demo.domain.travelPlan.service;

import com.example.demo.domain.travelPlan.dto.TravelPlanRequestDTO;
import com.example.demo.domain.travelPlan.dto.TravelPlanResponseDTO;
import com.example.demo.domain.travelPlan.entity.TravelPlan;
import com.example.demo.domain.travelPlan.repository.TravelPlanRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelPlanServiceImpl implements TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    @Override
    @Transactional
    public TravelPlanResponseDTO create(TravelPlanRequestDTO travelPlanRequestDTO) {
        TravelPlan travelPlan = TravelPlan.builder()
                .title(travelPlanRequestDTO.getTitle())
                .startDate(travelPlanRequestDTO.getStartDate())
                .endDate(travelPlanRequestDTO.getEndDate())
                .userId(travelPlanRequestDTO.getUserId())
                .isPublic(travelPlanRequestDTO.isPublic())
                .build();

        TravelPlan savePlan = travelPlanRepository.save(travelPlan);

        return TravelPlanResponseDTO.builder()
                .id(savePlan.getId())
                .title(savePlan.getTitle())
                .startDate(savePlan.getStartDate())
                .endDate(savePlan.getEndDate())
                .userId(savePlan.getUserId())
                .isPublic(savePlan.isPublic())
                .createdAt(savePlan.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public TravelPlanResponseDTO update(Long Id, TravelPlanRequestDTO travelPlanRequestDTO) {
        TravelPlan plan = travelPlanRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        plan.update(travelPlanRequestDTO.getTitle(), travelPlanRequestDTO.getStartDate(), travelPlanRequestDTO.getEndDate(), travelPlanRequestDTO.isPublic());

        return TravelPlanResponseDTO.builder()
                .id(plan.getId())
                .title(plan.getTitle())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .userId(plan.getUserId())
                .isPublic(plan.isPublic())
                .createdAt(plan.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void delete(Long Id) {
        if (!travelPlanRepository.existsById(Id)) {
            throw new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다.");
        }

        travelPlanRepository.deleteById(Id);
    }

    @Override
    public TravelPlanResponseDTO findById(Long Id) {
        TravelPlan travelPlan = travelPlanRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        return TravelPlanResponseDTO.builder()
                .id(travelPlan.getId())
                .title(travelPlan.getTitle())
                .startDate(travelPlan.getStartDate())
                .endDate(travelPlan.getEndDate())
                .userId(travelPlan.getUserId())
                .isPublic(travelPlan.isPublic())
                .build();
    }

    @Override
    public List<TravelPlanResponseDTO> findByUserId(Long userId) {
        List<TravelPlan> plans = travelPlanRepository.findByUserId(userId);

        return plans.stream()
                .map(plan -> TravelPlanResponseDTO.builder()
                        .id(plan.getId())
                        .title(plan.getTitle())
                        .startDate(plan.getStartDate())
                        .endDate(plan.getEndDate())
                        .userId(plan.getUserId())
                        .isPublic(plan.isPublic())
                        .createdAt(plan.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
