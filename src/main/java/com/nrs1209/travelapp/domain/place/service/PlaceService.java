package com.nrs1209.travelapp.domain.place.service;

import com.nrs1209.travelapp.domain.place.dto.PlaceRequestDTO;
import com.nrs1209.travelapp.domain.place.dto.PlaceResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceService {

    PlaceResponseDTO createPlace(PlaceRequestDTO requestDTO);
    PlaceResponseDTO getPlaceById(Long placeId);
    List<PlaceResponseDTO> searchPlacesByName(String name);
    void deletePlace(Long placeId);

    Page<PlaceResponseDTO> getAllPlaces(Pageable pageable);
}
