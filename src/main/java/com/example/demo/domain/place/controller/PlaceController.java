package com.example.demo.domain.place.controller;

import com.example.demo.domain.place.dto.PlaceRequestDTO;
import com.example.demo.domain.place.dto.PlaceResponseDTO;
import com.example.demo.domain.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    // 장소 생성 API
    @PostMapping
    public ResponseEntity<PlaceResponseDTO> createPlace(@Valid @RequestBody PlaceRequestDTO requestDTO) {
        PlaceResponseDTO responseDTO = placeService.createPlace(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // 모든 장소 목록 조회 API
    @GetMapping
    public ResponseEntity<List<PlaceResponseDTO>> getAllPlaces() {
        List<PlaceResponseDTO> responseDTOList = placeService.getAllPlaces();
        return ResponseEntity.ok(responseDTOList);
    }

    // 특정 ID로 장소 조회 API
    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceResponseDTO> getPlaceById(@PathVariable Long placeId) {
        PlaceResponseDTO responseDTO = placeService.getPlaceById(placeId);
        return ResponseEntity.ok(responseDTO);
    }

    // 이름으로 장소 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<PlaceResponseDTO>> getPlacesByName(@PathVariable String name) {
        List<PlaceResponseDTO> responseDTOList = placeService.searchPlacesByName(name);
        return ResponseEntity.ok(responseDTOList);
    }

    // 장소 삭제 API
    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long placeId) {
        placeService.deletePlace(placeId);
        return ResponseEntity.noContent().build();
    }
}
