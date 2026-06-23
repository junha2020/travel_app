package com.nrs1209.travelapp.domain.backpack.service;

import com.nrs1209.travelapp.domain.backpack.entity.Backpack;
import com.nrs1209.travelapp.domain.backpack.repository.BackpackRepository;
import com.nrs1209.travelapp.domain.place.entity.Place;
import com.nrs1209.travelapp.domain.place.repository.PlaceRepository;
import com.nrs1209.travelapp.domain.user.entity.User;
import com.nrs1209.travelapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BackpackService {

    private final BackpackRepository backpackRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    // 1. 배낭에 장소 담기 (찜하기)
    public void addBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));

        // 중복 검증
        if (backpackRepository.existsByUserAndPlace(user, place)) {
            throw new IllegalArgumentException("이미 배낭에 찜해둔 장소입니다.");
        }

        Backpack backpack = Backpack.builder()
                .user(user)
                .place(place)
                .build();

        backpackRepository.save(backpack);
    }

    // 2. 배낭에서 장소 빼기 (찜 취소)
    public void removeBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));

        Backpack backpack = backpackRepository.findByUserAndPlace(user, place)
                .orElseThrow(() -> new IllegalArgumentException("배낭에 찜해두지 않은 장소입니다."));

        backpackRepository.delete(backpack);
    }

    // 3. 배낭의 찜 전체 조회
    @Transactional(readOnly = true)
    public List<Place> getBackpackList(Long userId) {
        List<Backpack> backpacks = backpackRepository.findByUserId(userId);
        return backpacks.stream()
                .map(Backpack::getPlace)
                .collect(Collectors.toList());
    }
}
