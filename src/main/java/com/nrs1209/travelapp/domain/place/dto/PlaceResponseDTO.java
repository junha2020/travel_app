package com.nrs1209.travelapp.domain.place.dto;

import com.nrs1209.travelapp.domain.place.entity.Place;
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
