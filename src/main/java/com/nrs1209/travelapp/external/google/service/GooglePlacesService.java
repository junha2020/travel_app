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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    private final RestTemplate restTemplate;
    private final PlaceRepository placeRepository;

    @Value("${google.places.api.key:}")
    private String googleApiKey;

    private static final String GOOGLE_PLACES_SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    private static final Pattern PHOTO_NAME_PATTERN = Pattern.compile("^places/[A-Za-z0-9_-]+/photos/[A-Za-z0-9_-]+$");

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
            headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.photos,places.editorialSummary");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("textQuery", query);
            requestBody.put("languageCode", "ko");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            GooglePlacesDTO response = restTemplate.postForObject(GOOGLE_PLACES_SEARCH_URL, entity, GooglePlacesDTO.class);

            if (response != null && response.getPlaces() != null && !response.getPlaces().isEmpty()) {
                List <Place> newPlacesToSave = new ArrayList<>();

                for (GooglePlacesDTO.PlaceItem item : response.getPlaces()) {
                    if (item.getRating() == null || item.getPhotos() == null || item.getPhotos().isEmpty()) {
                        continue;
                    }

                    String name = item.getDisplayName() != null ? item.getDisplayName().getText() : "이름 없음";

                    if (!placeRepository.findByNameContainingIgnoreCase(name).isEmpty()) {
                        continue;
                    }

                    String address = item.getFormattedAddress() != null ? item.getFormattedAddress() : "";
                    Double lat = item.getLocation() != null ? item.getLocation().getLatitude() : null;
                    Double lng = item.getLocation() != null ? item.getLocation().getLongitude() : null;
                    Double rating = item.getRating();

                    // 이미지 부분 API 키 노출 방지를 위해 백엔드 프록시 경로로 저장
                    String photoName = item.getPhotos().get(0).getName();
                    String imageUrl = "/api/places/photo?name=" + photoName;

                    // 구글 공식 한 줄 설명 사용. 없으면 주소 표시
                    String description = (item.getEditorialSummary() != null && item.getEditorialSummary().getText() != null)
                            ? item.getEditorialSummary().getText()
                            : address;

                    // 카테고리 판별
                    String category = "관광지";
                    if (query.contains("식당") || query.contains("맛집")) {
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
                            .description(description)
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

    public byte[] fetchPlacePhoto(String photoName) {
        if (photoName == null || !PHOTO_NAME_PATTERN.matcher(photoName).matches()) {
            throw new IllegalArgumentException("올바르지 않은 사진 경로입니다.");
        }

        if (googleApiKey == null || googleApiKey.isBlank()) {
            throw new IllegalStateException("Google Places API 키가 설정되지 않았습니다.");
        }

        String photoUrl = String.format("https://places.googleapis.com/v1/%s/media?maxHeightPx=800&maxWidthPx=800", photoName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Goog-Api-Key", googleApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(photoUrl, HttpMethod.GET, entity, byte[].class).getBody();
    }
}
