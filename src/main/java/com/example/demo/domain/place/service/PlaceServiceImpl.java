package com.example.demo.domain.place.service;

import com.example.demo.domain.place.dto.PlaceRequestDTO;
import com.example.demo.domain.place.dto.PlaceResponseDTO;
import com.example.demo.domain.place.entity.Place;
import com.example.demo.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public PlaceResponseDTO createPlace(PlaceRequestDTO requestDTO) {
        Place place = Place.builder()
                .name(requestDTO.getName())
                .address(requestDTO.getAddress())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .build();

        Place savedPlace = placeRepository.save(place);
        return PlaceResponseDTO.fromEntity(savedPlace);
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceResponseDTO getPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다. ID: " + placeId));
        return PlaceResponseDTO.fromEntity(place);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceResponseDTO> searchPlacesByName(String name) {
        List<Place> places = placeRepository.findByNameContainingIgnoreCase(name);
        return places.stream()
                .map(PlaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePlace(Long placeId) {
        if (!placeRepository.existsById(placeId)) {
            throw new IllegalArgumentException("삭제할 장소를 찾을 수 없습니다. ID: " + placeId);
        }
        // TODO: 이 장소 참조하는 PlanPlace가 있는지 확인 후 지우는 로직도 추가해야 함
        placeRepository.deleteById(placeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceResponseDTO> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return places.stream()
                .map(PlaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
