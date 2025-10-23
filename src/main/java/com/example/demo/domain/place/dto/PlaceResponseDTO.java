package com.example.demo.domain.place.dto;

import com.example.demo.domain.place.entity.Place;
import com.example.demo.domain.travelPlan.entity.TravelPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDTO {

    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    public static PlaceResponseDTO fromEntity(Place place) {
        return PlaceResponseDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }

}
