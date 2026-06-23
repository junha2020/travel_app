package com.nrs1209.travelapp.domain.backpack.controller;

import com.nrs1209.travelapp.domain.backpack.service.BackpackService;
import com.nrs1209.travelapp.domain.place.dto.PlaceResponseDTO;
import com.nrs1209.travelapp.domain.place.entity.Place;
import com.nrs1209.travelapp.domain.user.entity.User;
import com.nrs1209.travelapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backpack")
@RequiredArgsConstructor
public class BackpackController {

    private final BackpackService backpackService;
    private final UserRepository userRepository;

    // 1. 배낭에 찜 추가
    @PostMapping("/add")
    public ResponseEntity<Void> addBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long placeId) {

        // 로그인 된 user_name으로 회원 정보 조회
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        backpackService.addBookmark(user.getId(), placeId);
        return ResponseEntity.ok().build();
    }

    // 2. 배낭에서 찜 해제
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long placeId) {

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        backpackService.removeBookmark(user.getId(), placeId);
        return ResponseEntity.ok().build();
    }

    // 3. 내 배낭 목록 조회
    @GetMapping
    public ResponseEntity<List<PlaceResponseDTO>> getBackpackList(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Place> places = backpackService.getBackpackList(user.getId());

        // Place -> PlaceResponseDTO로 변환해 순환 참조 방지
        List<PlaceResponseDTO> responseDTOS = places.stream()
                .map(PlaceResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOS);
    }
}
