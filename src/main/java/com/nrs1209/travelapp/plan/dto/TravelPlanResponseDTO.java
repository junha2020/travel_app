package com.nrs1209.travelapp.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlanResponseDTO {

    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
    private boolean isPublic;
    private LocalDateTime createdAt;

    private List<PlaceInPlanDTO> places;

}
