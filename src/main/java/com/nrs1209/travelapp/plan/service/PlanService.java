package com.nrs1209.travelapp.plan.service;

import com.nrs1209.travelapp.place.entity.Place;
import com.nrs1209.travelapp.place.repository.PlaceRepository;
import com.nrs1209.travelapp.plan.entity.PlanPlace;
import com.nrs1209.travelapp.plan.repository.PlanPlaceRepository;
import com.nrs1209.travelapp.plan.dto.AddPlaceRequestDTO;
import com.nrs1209.travelapp.plan.dto.PlaceInPlanDTO;
import com.nrs1209.travelapp.plan.dto.TravelPlanRequestDTO;
import com.nrs1209.travelapp.plan.dto.TravelPlanResponseDTO;
import com.nrs1209.travelapp.plan.dto.UpdatePlaceSequenceDTO;
import com.nrs1209.travelapp.plan.entity.Plan;
import com.nrs1209.travelapp.plan.repository.PlanRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlaceRepository placeRepository;
    private final PlanPlaceRepository planPlaceRepository;

    @Transactional
    public TravelPlanResponseDTO create(TravelPlanRequestDTO travelPlanRequestDTO) {
        Plan plan = Plan.builder()
                .title(travelPlanRequestDTO.getTitle())
                .startDate(travelPlanRequestDTO.getStartDate())
                .endDate(travelPlanRequestDTO.getEndDate())
                .userId(travelPlanRequestDTO.getUserId())
                .isPublic(travelPlanRequestDTO.isPublic())
                .build();

        Plan savePlan = planRepository.save(plan);

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

    @Transactional
    public TravelPlanResponseDTO update(Long Id, TravelPlanRequestDTO travelPlanRequestDTO) {
        Plan plan = planRepository.findById(Id)
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

    @Transactional
    public void delete(Long Id) {
        if (!planRepository.existsById(Id)) {
            throw new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다.");
        }

        planRepository.deleteById(Id);
    }

    @Transactional(readOnly = true)
    public TravelPlanResponseDTO findById(Long Id) {
        Plan plan = planRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

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
                .places(placeDTOs)
                .build();
    }

    @Transactional(readOnly = true)
    public List<TravelPlanResponseDTO> findByUserId(Long userId) {
        List<Plan> plans = planRepository.findByUserId(userId);

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

    public void addPlaceToPlan(Long planId, AddPlaceRequestDTO requestDTO) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));
        Place place = placeRepository.findById(requestDTO.getPlaceId()).orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));
        PlanPlace planPlace = PlanPlace.builder()
                .plan(plan)
                .place(place)
                .day(requestDTO.getDay())
                .build();

        planPlaceRepository.save(planPlace);
    }

    public void removePlaceFromPlan(Long planId, Long planPlaceId) {
        PlanPlace planPlace = planPlaceRepository.findById(planPlaceId).orElseThrow(() -> new IllegalArgumentException("해당 장소 연결 정보를 찾을 수 없습니다."));

        if (!planPlace.getPlan().getId().equals(planId)) {
            throw new IllegalArgumentException("해당 여행 계획에 속한 장소가 아닙니다.");
        }

        planPlaceRepository.deleteById(planPlaceId);
    }

    @Transactional
    public void updatePlacesSequence(Long planId, List<UpdatePlaceSequenceDTO> sequenceDTOs) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        for (UpdatePlaceSequenceDTO dto : sequenceDTOs) {
            PlanPlace planPlace = planPlaceRepository.findById(dto.getPlanPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 장소 연결 정보를 찾을 수 없습니다."));

            if (!planPlace.getPlan().getId().equals(planId)) {
                throw new IllegalArgumentException("해당 여행 계획에 속한 장소가 아닙니다.");
            }

            planPlace.setDay(dto.getDay());
            planPlace.setSequence(dto.getSequence());
        }
    }

}
