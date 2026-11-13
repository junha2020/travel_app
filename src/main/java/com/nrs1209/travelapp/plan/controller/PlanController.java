package com.nrs1209.travelapp.plan.controller;

import com.nrs1209.travelapp.plan.dto.AddPlaceRequestDTO;
import com.nrs1209.travelapp.plan.dto.TravelPlanRequestDTO;
import com.nrs1209.travelapp.plan.dto.TravelPlanResponseDTO;
import com.nrs1209.travelapp.plan.dto.UpdatePlaceSequenceDTO;
import com.nrs1209.travelapp.plan.service.GeminiService;
import com.nrs1209.travelapp.plan.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final GeminiService geminiService;

    // 여행 계획 생성
    @PostMapping
    public ResponseEntity<TravelPlanResponseDTO> createPlan(@Valid @RequestBody TravelPlanRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        TravelPlanResponseDTO responseDTO = planService.create(requestDTO, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // 특정 ID로 여행 계획 조회
    @GetMapping("/{planId}")
    public ResponseEntity<TravelPlanResponseDTO> getPlanById(@PathVariable Long planId, @AuthenticationPrincipal UserDetails userDetails) {
        TravelPlanResponseDTO responseDTO = planService.findById(planId, userDetails.getUsername());
        return ResponseEntity.ok(responseDTO);
    }

    // 특정 사용자의 모든 여행 계획 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TravelPlanResponseDTO>> getPlanByUserId(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        List<TravelPlanResponseDTO> responseDTOList = planService.findByUserId(userId, userDetails.getUsername());
        return ResponseEntity.ok(responseDTOList);
    }

    // 여행 계획 수정
    @PutMapping("/{planId}")
    public ResponseEntity<TravelPlanResponseDTO> updatePlan(@PathVariable Long planId, @Valid @RequestBody TravelPlanRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        TravelPlanResponseDTO responseDTO = planService.update(planId, requestDTO, userDetails.getUsername());
        return ResponseEntity.ok(responseDTO);
    }

    // 여행 계획 삭제
    @DeleteMapping("/{planId}")
    public ResponseEntity<TravelPlanResponseDTO> deletePlan(@PathVariable Long planId, @AuthenticationPrincipal UserDetails userDetails) {
        planService.delete(planId, userDetails.getUsername());
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 응답
    }

    // 계획에 장소 추가
    @PostMapping("/{planId}/places")
    public ResponseEntity<Void> addPlace(@PathVariable Long planId, @Valid @RequestBody AddPlaceRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        planService.addPlaceToPlan(planId, requestDTO, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 계획에서 장소 삭제
    @DeleteMapping("/{planId}/places/{planPlaceId}")
    public ResponseEntity<Void> removePlace(@PathVariable Long planId, @PathVariable Long planPlaceId, @AuthenticationPrincipal UserDetails userDetails) {
        planService.removePlaceFromPlan(planId, planPlaceId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // 여행 일정 내 장소들의 순서 및 날짜 일괄 업데이트 API
    @PutMapping("/{planId}/places/sequence")
    public ResponseEntity<Void> updatePlacesSequence(
            @PathVariable Long planId,
            @RequestBody List<UpdatePlaceSequenceDTO> sequenceDTOs,
            @AuthenticationPrincipal UserDetails userDetails) {
        planService.updatePlacesSequence(planId, sequenceDTOs, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    // 배낭 찜 장소 일괄 벌크 추가 API
    @PostMapping("/{planId}/places/bulk")
    public ResponseEntity<Void> addPlacesBulk(
            @PathVariable Long planId,
            @Valid @RequestBody List<AddPlaceRequestDTO> requestDTOs,
            @AuthenticationPrincipal UserDetails userDetails) {
        planService.addPlacesToPlanBulk(planId, requestDTOs, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 추천 일정 복사 API
    @PostMapping("/{recommendPlanId}/copy")
    public ResponseEntity<Long> copyRecommendPlan(
            @PathVariable Long recommendPlanId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long newPlanId = planService.copyPlan(recommendPlanId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(newPlanId);
    }

    // 실시간 AI 일정 생성 API 추가
    @PostMapping("/ai-generate")
    public ResponseEntity<Long> generateAIPlan(
            @RequestParam String cityName,
            @RequestParam int days,
            @RequestParam String theme,
            @RequestParam Long userId) {
        Long newPlanId = geminiService.generateAIPlan(cityName, days, theme, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPlanId);
    }
}
