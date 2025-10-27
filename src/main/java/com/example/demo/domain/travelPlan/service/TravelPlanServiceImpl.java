package com.example.demo.domain.travelPlan.service;

import com.example.demo.domain.place.entity.Place;
import com.example.demo.domain.place.repository.PlaceRepository;
import com.example.demo.domain.placePlan.entitiy.PlanPlace;
import com.example.demo.domain.placePlan.repository.PlanPlaceRepository;
import com.example.demo.domain.travelPlan.dto.AddPlaceRequestDTO;
import com.example.demo.domain.travelPlan.dto.PlaceInPlanDTO;
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
    private final PlaceRepository placeRepository;
    private final PlanPlaceRepository planPlaceRepository;

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
    @Transactional(readOnly = true)
    public TravelPlanResponseDTO findById(Long Id) {
        TravelPlan travelPlan = travelPlanRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        List<PlaceInPlanDTO> placeDTOs = travelPlan.getPlanPlace().stream()
                .map(planPlace -> PlaceInPlanDTO.builder()
                        .planPlaceId(planPlace.getId())
                        .placeId(planPlace.getPlace().getId())
                        .name(planPlace.getPlace().getName())
                        .address(planPlace.getPlace().getAddress())
                        .latitude(planPlace.getPlace().getLatitude())
                        .longitude(planPlace.getPlace().getLongitude())
                        .day(planPlace.getDay())
                        .sequence(planPlace.getSequence())
                        .memo(planPlace.getMemo())
                        .build())
                .collect(Collectors.toList());

        return TravelPlanResponseDTO.builder()
                .id(travelPlan.getId())
                .title(travelPlan.getTitle())
                .startDate(travelPlan.getStartDate())
                .endDate(travelPlan.getEndDate())
                .userId(travelPlan.getUserId())
                .isPublic(travelPlan.isPublic())
                .places(placeDTOs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelPlanResponseDTO> findByUserId(Long userId) {
        List<TravelPlan> plans = travelPlanRepository.findByUserId(userId);

        return plans.stream()
                .map(plan -> {
                    List<PlaceInPlanDTO> placeDTOs = plan.getPlanPlace().stream()
                            .map(planPlace -> PlaceInPlanDTO.builder()
                                    .planPlaceId(planPlace.getId())
                                    .placeId(planPlace.getPlace().getId())
                                    .name(planPlace.getPlace().getName())
                                    .address(planPlace.getPlace().getAddress())
                                    .latitude(planPlace.getPlace().getLatitude())
                                    .longitude(planPlace.getPlace().getLongitude())
                                    .day(planPlace.getDay())
                                    .sequence(planPlace.getSequence())
                                    .memo(planPlace.getMemo())
                                    .build())
                            .collect(Collectors.toList());

                    return TravelPlanResponseDTO.builder()
                            .id(plan.getId())
                            .title(plan.getTitle())
                            .startDate(plan.getStartDate())
                            .endDate(plan.getEndDate())
                            .userId(plan.getUserId())
                            .isPublic(plan.isPublic())
                            .createdAt(plan.getCreatedAt())
                            .places(placeDTOs)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addPlaceToPlan(Long planId, AddPlaceRequestDTO requestDTO) {
        TravelPlan plan = travelPlanRepository.findById(planId).orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));
        Place place = placeRepository.findById(requestDTO.getPlaceId()).orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));
        PlanPlace planPlace = PlanPlace.builder()
                .travelPlan(plan)
                .place(place)
                .day(requestDTO.getDay())
                .build();

        planPlaceRepository.save(planPlace);
    }

    @Override
    public void removePlaceFromPlan(Long planId, Long planPlaceId) {
        PlanPlace planPlace = planPlaceRepository.findById(planPlaceId).orElseThrow(() -> new IllegalArgumentException("해당 장소 연결 정보를 찾을 수 없습니다."));

        if (!planPlace.getTravelPlan().getId().equals(planId)) {
            throw new IllegalArgumentException("해당 여행 계획에 속한 장소가 아닙니다.");
        }

        planPlaceRepository.deleteById(planPlaceId);
    }

}
