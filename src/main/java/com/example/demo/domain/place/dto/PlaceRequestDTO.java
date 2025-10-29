package com.example.demo.domain.place.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRequestDTO {

    @NotBlank(message = "장소 이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "장소 주소는 필수 입력 값입니다.")
    private String address;

    private Double latitude;
    private Double longitude;

}
