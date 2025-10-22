package com.example.demo.domain.travelPlan.dto;

import com.example.demo.domain.travelPlan.entity.TravelPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public TravelPlanResponseDTO(TravelPlan plan) {
        this.id = plan.getId();
        this.title = plan.getTitle();
        this.startDate = plan.getStartDate();
        this.endDate = plan.getEndDate();
        this.userId = plan.getUserId();
    }

}
