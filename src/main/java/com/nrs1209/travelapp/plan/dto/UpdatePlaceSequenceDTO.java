package com.nrs1209.travelapp.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlaceSequenceDTO {
    private Long planPlaceId;
    private int day;
    private int sequence;
}
