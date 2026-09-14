package com.nrs1209.travelapp.external.google.service;

import com.nrs1209.travelapp.external.google.dto.GooglePlacesDTO;
import com.nrs1209.travelapp.place.dto.PlaceResponseDTO;
import com.nrs1209.travelapp.place.entity.Place;
import com.nrs1209.travelapp.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    private final RestTemplate restTemplate;
    private final PlaceRepository placeRepository;

    @Value("${google.places.api.key:}")
    private String googleApiKey;

    private static final String GOOGLE_PLACES_SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";

    /**
     * DB에 있는지 먼저 조회 -> 없으면 API 호출해 DB에 저장
     */
    public List<PlaceResponseDTO> searchPlacesWithAutoCache(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        // 기존 DB 먼저 확인
        List<Place> existingPlaces = placeRepository.findByNameContainingIgnoreCase(query);
        if (!existingPlaces.isEmpty()) {
            log.info("DB에 존재합니다. [{}]: {}개 반환)", query, existingPlaces.size());
            return existingPlaces.stream().map(PlaceResponseDTO::fromEntity).collect(Collectors.toList());
        }

        // 키 없으면 빈 리스트 반환
        if (googleApiKey == null || googleApiKey.isBlank()) {
            log.warn("google.places.api.key 가 설정되지 않았습니다. application-secret.properties를 확인하세요.");
            return List.of();
        }

        // Google Places API 호출
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", googleApiKey);
            headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.photos");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("textQuery", query);
            requestBody.put("languageCode", "ko");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            GooglePlacesDTO response = restTemplate.postForObject(GOOGLE_PLACES_SEARCH_URL, entity, GooglePlacesDTO.class);

            if (response != null && response.getPlaces() != null && !response.getPlaces().isEmpty()) {
                List <Place> newPlacesToSave = new ArrayList<>();

                for (GooglePlacesDTO.PlaceItem item : response.getPlaces()) {
                    String name = item.getDisplayName() != null ? item.getDisplayName().getText() : "이름 없음";
                    String address = item.getFormattedAddress() != null ? item.getFormattedAddress() : "";
                    Double lat = item.getLocation() != null ? item.getLocation().getLatitude() : null;
                    Double lng = item.getLocation() != null ? item.getLocation().getLongitude() : null;
                    Double rating = item.getRating() != null ? item.getRating() : 4.5;

                    // 구글 고화질 사진 URL 조립
                    String imageUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80"; // fallback
                    if (item.getPhotos() != null && !item.getPhotos().isEmpty()) {
                        String photoName = item.getPhotos().get(0).getName();
                        imageUrl = String.format("https://places.googleapis.com/v1/%s/media?maxHeightPx=800&maxWidthPx=800&key=%s", photoName, googleApiKey);
                    }

                    // 카테고리 판별
                    String category = "관광지";
                    if (query.contains("식당")) {
                        category = "맛집";
                    } else if (query.contains("호텔")) {
                        category = "숙소";
                    }

                    Place place = Place.builder()
                            .name(name)
                            .address(address)
                            .latitude(lat)
                            .longitude(lng)
                            .imageUrl(imageUrl)
                            .description(String.format("구글 실시간 평점 * %.1f | %s", rating, address))
                            .category(category)
                            .rating(rating)
                            .build();

                    newPlacesToSave.add(place);
                }

                // DB에 영구 저장
                List<Place> savedPlaces = placeRepository.saveAll(newPlacesToSave);
                log.info("Google Places API에서 가져와서 DB에 저장. [{}]: {}개 저장됨", query, savedPlaces.size());

                return savedPlaces.stream().map(PlaceResponseDTO::fromEntity).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Google Places API 호출 실패: {}", e.getMessage());
        }

        return List.of();
    }
}
