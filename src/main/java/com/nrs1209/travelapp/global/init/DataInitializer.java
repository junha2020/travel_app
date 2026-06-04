package com.nrs1209.travelapp.global.init;

import com.nrs1209.travelapp.domain.place.entity.Place;
import com.nrs1209.travelapp.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PlaceRepository placeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (placeRepository.count() == 0) {
            Place tokyoTower = Place.builder()
                    .name("도쿄 타워")
                    .address("4 Chome-2-8 Shibakoen, Minato City, Tokyo 105-0011")
                    .category("관광지")
                    .description("도쿄의 상징적인 붉은 전파탑으로 멋진 도심 야경을 감상할 수 있는 곳")
                    .rating(4.6)
                    .imageUrl("https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80")
                    .build();

            Place ichiranRamen = Place.builder()
                    .name("이치란 라멘")
                    .address("3 Chome-34-11 Shinjuku, Tokyo 160-0022")
                    .category("음식점")
                    .description("나만의 커스텀 레시피로 깊은 돈코츠 라멘을 맛볼 수 있는 유명 맛집")
                    .rating(4.4)
                    .imageUrl("https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=800&q=80")
                    .build();

            Place shinjukuGyoen = Place.builder()
                    .name("신주쿠 교엔")
                    .address("11 Naitomachi, Shinjuku City, Tokyo 160-0014")
                    .category("관광지")
                    .description("넓고 아름다운 정원과 온실이 있어 도심 속 여유를 만끽할 수 있는 국립 정원")
                    .rating(4.7)
                    .imageUrl("https://images.unsplash.com/photo-1542931287-023b922fa89b?auto=format&fit=crop&w=800&q=80")
                    .build();

            Place shibuyaCrossing = Place.builder()
                    .name("시부야 스크램블 교차로")
                    .address("1 Chome-2-1 Dogenzaka, Shibuya City, Tokyo 150-0043")
                    .category("명소")
                    .description("도쿄 시부야의 활기차고 분주한 세계 최대 규모의 횡단보도")
                    .rating(4.5)
                    .imageUrl("https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&w=800&q=80")
                    .build();

            List<Place> dummyPlaces = Arrays.asList(tokyoTower, ichiranRamen, shinjukuGyoen, shibuyaCrossing);
            placeRepository.saveAll(dummyPlaces);
            System.out.println("초기 데이터(더미 장소 4개) 추가 완료!");
        }
    }
}
