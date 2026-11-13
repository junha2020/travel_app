// TODO [2단계]: findById() 및 findByUserId()의 PlaceInPlanDTO 빌더 매핑에 .category(planPlace.getPlace().getCategory()) 및 .imageUrl(planPlace.getPlace().getImageUrl()) 주입하기
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
import com.nrs1209.travelapp.user.entity.User;
import com.nrs1209.travelapp.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private final EntityManager entityManager; // 네이티브 쿼리 실행용 엔티티 매니저 주입
    private final UserRepository userRepository;

    @Transactional
    public TravelPlanResponseDTO create(TravelPlanRequestDTO travelPlanRequestDTO, String username) {
        User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Plan plan = Plan.builder()
                .title(travelPlanRequestDTO.getTitle())
                .startDate(travelPlanRequestDTO.getStartDate())
                .endDate(travelPlanRequestDTO.getEndDate())
                .userId(currentUser.getId())
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
    public TravelPlanResponseDTO update(Long Id, TravelPlanRequestDTO travelPlanRequestDTO, String username) {
        Plan plan = validatePlanOwnership(Id, username);

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
    public void delete(Long Id, String username) {
        // 계획 조회
        Plan plan = validatePlanOwnership(Id, username);

        // 내 계획이 맞으니 안전하게 삭제
        planPlaceRepository.deleteByPlan_Id(Id);
        planRepository.deleteById(Id);

        // DELETE 쿼리 미리 flush로 반영.
        entityManager.flush();

        // 모두 지우면 Auto-Increment 값 1로 초기화
        if (planRepository.count() == 0) {
            entityManager.createNativeQuery("ALTER TABLE plan AUTO_INCREMENT = 1").executeUpdate();
        }
    }

    @Transactional(readOnly = true)
    public TravelPlanResponseDTO findById(Long Id, String username) {
        Plan plan = planRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        if (!plan.isPublic()) {
            User currentUser = userRepository.findByUserName(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            if (!plan.getUserId().equals(currentUser.getId())) {
                throw new org.springframework.security.access.AccessDeniedException("비공개 여행 계획에 대한 접근 권한이 없습니다.");
            }
        }

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
                        .category(planPlace.getPlace().getCategory())
                        .imageUrl(planPlace.getPlace().getImageUrl())
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
    public List<TravelPlanResponseDTO> findByUserId(Long userId, String username) {
        User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!currentUser.getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("다른 사용자의 여행 계획 목록은 조회할 수 없습니다.");
        }

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
                                    .category(planPlace.getPlace().getCategory())
                                    .imageUrl(planPlace.getPlace().getImageUrl())
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

    public void addPlaceToPlan(Long planId, AddPlaceRequestDTO requestDTO, String username) {
        Plan plan = validatePlanOwnership(planId, username);
        Place place = placeRepository.findById(requestDTO.getPlaceId()).orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));
        PlanPlace planPlace = PlanPlace.builder()
                .plan(plan)
                .place(place)
                .day(requestDTO.getDay())
                .build();

        planPlaceRepository.save(planPlace);
    }

    public void removePlaceFromPlan(Long planId, Long planPlaceId, String username) {
        validatePlanOwnership(planId, username);
        PlanPlace planPlace = planPlaceRepository.findById(planPlaceId).orElseThrow(() -> new IllegalArgumentException("해당 장소 연결 정보를 찾을 수 없습니다."));

        if (!planPlace.getPlan().getId().equals(planId)) {
            throw new IllegalArgumentException("해당 여행 계획에 속한 장소가 아닙니다.");
        }

        planPlaceRepository.deleteById(planPlaceId);
    }

    @Transactional
    public void updatePlacesSequence(Long planId, List<UpdatePlaceSequenceDTO> sequenceDTOs, String username) {
        Plan plan = validatePlanOwnership(planId, username);

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

    @Transactional
    public void addPlacesToPlanBulk(Long planId, List<AddPlaceRequestDTO> requestDTOs, String username) {
        Plan plan = validatePlanOwnership(planId, username);

        List<PlanPlace> planPlaces = requestDTOs.stream().map(dto -> {
            Place place = placeRepository.findById(dto.getPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다. ID: " + dto.getPlaceId()));

            return PlanPlace.builder()
                    .plan(plan)
                    .place(place)
                    .day(dto.getDay())
                    .build();
        }).collect(Collectors.toList());

        planPlaceRepository.saveAll(planPlaces);
    }

    @Transactional
    public Long copyPlan(Long recommendPlanId, String username) {
        User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 복사할 원본 추천 일정 조회
        Plan sourcePlan = planRepository.findById(recommendPlanId)
                .orElseThrow(() -> new IllegalArgumentException("해당 추천 일정을 찾을 수 없습니다."));

        // 대상 유저 소유의 새 Plan 복제 생성
        Plan targetPlan = Plan.builder()
                .title("[AI 추천] " + sourcePlan.getTitle())
                .startDate(sourcePlan.getStartDate())
                .endDate(sourcePlan.getEndDate())
                .userId(currentUser.getId())
                .isPublic(false)
                .build();

        Plan savedPlan = planRepository.save(targetPlan);

        // 원본 일정에 속해 있던 자식 장소들 깊은 복사 해 새 플랜에 매핑
        List<PlanPlace> copiedPlaces = sourcePlan.getPlanPlace().stream().map(sourcePlace ->
                PlanPlace.builder()
                        .plan(savedPlan)
                        .place(sourcePlace.getPlace())
                        .day(sourcePlace.getDay())
                        .sequence(sourcePlace.getSequence())
                        .build()
        ).collect(Collectors.toList());

        planPlaceRepository.saveAll(copiedPlaces);

        return savedPlan.getId();
    }

    private Plan validatePlanOwnership(Long planId, String username) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 계획을 찾을 수 없습니다."));

        User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!plan.getUserId().equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("해당 여행 계획에 대한 권한이 없습니다.");
        }

        return plan;
    }
}
