package com.example.demo.domain.travelPlan.controller;

import com.example.demo.domain.travelPlan.dto.TravelPlanRequestDTO;
import com.example.demo.domain.travelPlan.dto.TravelPlanResponseDTO;
import com.example.demo.domain.travelPlan.service.TravelPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    // 여행 계획 생성
    @PostMapping
    public ResponseEntity<TravelPlanResponseDTO> createPlan(@Valid @RequestBody TravelPlanRequestDTO requestDTO) {
        TravelPlanResponseDTO responseDTO = travelPlanService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // 특정 ID로 여행 계획 조회
    @GetMapping("/{planId}")
    public ResponseEntity<TravelPlanResponseDTO> getPlanById(@PathVariable Long planId) {
        TravelPlanResponseDTO responseDTO = travelPlanService.findById(planId);
        return ResponseEntity.ok(responseDTO);
    }

    // 특정 사용자의 모든 여행 계획 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TravelPlanResponseDTO>> getPlanByUserId(@PathVariable Long userId) {
        List<TravelPlanResponseDTO> responseDTOList = travelPlanService.findByUserId(userId);
        return ResponseEntity.ok(responseDTOList);
    }

    // 여행 계획 수정
    @PutMapping("/{planId}")
    public ResponseEntity<TravelPlanResponseDTO> updatePlan(@PathVariable Long planId, @Valid @RequestBody TravelPlanRequestDTO requestDTO) {
        TravelPlanResponseDTO responseDTO = travelPlanService.update(planId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // 여행 계획 삭제
    @DeleteMapping("/{planId}")
    public ResponseEntity<TravelPlanResponseDTO> deletePlan(@PathVariable Long planId) {
        travelPlanService.delete(planId);
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 응답
    }
}
