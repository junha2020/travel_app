package com.example.demo.domain.travelPlan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceInPlanDTO {

    private Long planPlaceId;
    private Long placeId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private int day;

    private int sequence;
    private String memo;

}