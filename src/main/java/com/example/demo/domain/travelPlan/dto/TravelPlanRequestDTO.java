package com.example.demo.domain.travelPlan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlanRequestDTO {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "시작 일자는 필수 입력 값입니다.")
    private LocalDate startDate;

    @NotBlank(message = "종료 일자는 필수 입력 값입니다.")
    private LocalDate endDate;

    @NotBlank(message = "사용자 ID가 필요합니다.")
    private Long userId;

    private boolean isPublic = false;

}