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
import com.nrs1209.travelapp.plan.entity.TravelPlan;
import com.nrs1209.travelapp.plan.repository.TravelPlanRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final PlaceRepository placeRepository;
    private final PlanPlaceRepository planPlaceRepository;

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

    @Transactional
    public void delete(Long Id) {
        if (!travelPlanRepository.existsById(Id)) {
            throw new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다.");
        }

        travelPlanRepository.deleteById(Id);
    }

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

    public void removePlaceFromPlan(Long planId, Long planPlaceId) {
        PlanPlace planPlace = planPlaceRepository.findById(planPlaceId).orElseThrow(() -> new IllegalArgumentException("해당 장소 연결 정보를 찾을 수 없습니다."));

        if (!planPlace.getTravelPlan().getId().equals(planId)) {
            throw new IllegalArgumentException("해당 여행 계획에 속한 장소가 아닙니다.");
        }

        planPlaceRepository.deleteById(planPlaceId);
    }

    @Transactional
    public void updatePlacesSequence(Long planId, List<UpdatePlaceSequenceDTO> sequenceDTOs) {
        TravelPlan plan = travelPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        for (UpdatePlaceSequenceDTO dto : sequenceDTOs) {
            PlanPlace planPlace = planPlaceRepository.findById(dto.getPlanPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 장소 연결 정보를 찾을 수 없습니다."));

            if (!planPlace.getTravelPlan().getId().equals(planId)) {
                throw new IllegalArgumentException("해당 여행 계획에 속한 장소가 아닙니다.");
            }

            planPlace.setDay(dto.getDay());
            planPlace.setSequence(dto.getSequence());
        }
    }

}
