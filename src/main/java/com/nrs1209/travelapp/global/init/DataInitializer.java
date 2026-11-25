package com.nrs1209.travelapp.global.init;

import com.nrs1209.travelapp.external.google.service.GooglePlacesService;
import com.nrs1209.travelapp.place.entity.Place;
import com.nrs1209.travelapp.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PlaceRepository placeRepository;
    private final GooglePlacesService googlePlacesService;

    @Override
    public void run(String... args) throws Exception {
        if (placeRepository.count() == 0) {
            log.info("[DataInitializer] DB가 비어있어 Google Places 실데이터 초기 적재를 시작합니다...");
            List<String> seedQueries = List.of(
                    "도쿄 유명 관광지", "도쿄 유명 맛집",
                    "오사카 유명 관광지", "오사카 유명 맛집",
                    "후쿠오카 유명 관광지", "후쿠오카 유명 맛집",
                    "삿포로 유명 관광지", "삿포로 유명 맛집"
            );

            for (String query: seedQueries) {
                googlePlacesService.searchPlacesWithAutoCache(query);
            }

            log.info("[DataInitializer] Google Places 실데이터 적재 완료! 총 {}개 장소 저장됨.", placeRepository.count());
        }
    }
}
