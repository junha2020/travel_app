package com.nrs1209.travelapp.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanPlaceSequenceDTO {

    private Long planPlaceId;
    private int day;
    private int sequence;
}
