// TODO [1단계]: PlaceInPlanDTO에 category(카테고리 핀 구분용)와 imageUrl(장소 썸네일용) 필드 추가하기
package com.nrs1209.travelapp.plan.dto;

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

    private String category;
    private String imageUrl;
}