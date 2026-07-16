package com.nrs1209.travelapp.plan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrs1209.travelapp.place.entity.Place;
import com.nrs1209.travelapp.place.repository.PlaceRepository;
import com.nrs1209.travelapp.plan.entity.Plan;
import com.nrs1209.travelapp.plan.entity.PlanPlace;
import com.nrs1209.travelapp.plan.repository.PlanPlaceRepository;
import com.nrs1209.travelapp.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final PlanRepository planRepository;
    private final PlanPlaceRepository planPlaceRepository;
    private final PlaceRepository placeRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Long generateAIPlan(String cityName, int days, String theme, Long userId) {
        // 엔드포인트
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=" + apiKey;

        List<Place> allPlaces = placeRepository.findAll();
        List<Place> cityPlaces = allPlaces.stream()
                .filter(p -> p.getAddress() != null && p.getAddress().contains(cityName))
                .toList();

        List<String> placeNames = cityPlaces.stream().map(Place::getName).toList();
        String availablePlacesStr = String.join(", ", placeNames);

        String prompt = String.format(
                "도시명 '%s'에 대해 %d일간의 추천 여행 동선 코스를 짜줘. " +
            "반환 양식은 반드시 아래의 JSON Array 규격만을 준수해야 해. 마크다운 울타리(```json)나 부가 설명 없이 순수 JSON 텍스트만 출력해 줘.\n" +
            "[\n" +
            "  {\n" +
            "    \"placeName\": \"실제 DB에 존재할 만한 유명한 관광지명\",\n" +
            "    \"day\": 1,\n" +
            "    \"sequence\": 1\n" +
            "  }\n" +
            "]\n" +
            "주의: 관광지명(placeName)은 반드시 해당 도시의 가장 유명하고 대표적인 명소 명칭(예: 도쿄 타워, 센소지, 오사카 성, 도톤보리 등)이어야 해.",
            cityName, days, theme, availablePlacesStr
        );

        // API 요청 바디 조립
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Gemini API 전송 및 응답 수신
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();

            // 응답 텍스트 추출 및 JSON 가공
            List<Map> candidates = (List<Map>) body.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            String rawJson = (String) parts.get(0).get("text");

            rawJson = rawJson.replace("```json", "").replace("```", "").trim(); // 마크다운 제거

            // JSON 파싱
            List<Map<String, Object>> rawPlaces = objectMapper.readValue(rawJson, List.class);

            // 새 Plan 마스터 정보 적재
            Plan newPlan = Plan.builder()
                    .title("[AI 추천]" + cityName + " " + days + "일 여행 코스")
                    .startDate(java.time.LocalDate.now())
                    .endDate(java.time.LocalDate.now().plusDays(days - 1))
                    .userId(userId)
                    .isPublic(false)
                    .build();

            Plan savedPlan = planRepository.save(newPlan);

            // 파싱된 장소 매핑 저장
            List<PlanPlace> planPlaces = new ArrayList<>();
            for (Map<String, Object> rawPlace : rawPlaces) {
                String placeName = (String) rawPlace.get("placeName");
                int day = ((Number) rawPlace.get("day")).intValue();
                int sequence = ((Number) rawPlace.get("sequence")).intValue();

                // DB에 해당 이름 장소 있는지 조회
                Place place = cityPlaces.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(placeName)
                                || p.getName().contains(placeName)
                                ||placeName.contains(p.getName()))
                        .findFirst()
                        .orElse(null);

                if (place != null) {
                    planPlaces.add(PlanPlace.builder()
                            .plan(savedPlan)
                            .place(place)
                            .day(day)
                            .sequence(sequence)
                            .build());
                }
            }

            planPlaceRepository.saveAll(planPlaces);
            return savedPlan.getId();
        } catch (Exception e) {
            e.printStackTrace();;
            throw new RuntimeException("AI 일정 생성 실패: " + e.getMessage());
        }
    }
}
