package com.example.demo.domain.place.service;

import com.example.demo.domain.place.dto.PlaceRequestDTO;
import com.example.demo.domain.place.dto.PlaceResponseDTO;

import java.util.List;

public interface PlaceService {

    PlaceResponseDTO createPlace(PlaceRequestDTO requestDTO);
    PlaceResponseDTO getPlaceById(Long placeId);
    List<PlaceResponseDTO> searchPlacesByName(String name);
    void deletePlace(Long placeId);
}
