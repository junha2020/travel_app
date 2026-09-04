package com.nrs1209.travelapp.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDTO {

    private String cityName;
    private double temperature;
    private int humidity;
    private String weatherText;
    private String weatherEmoji;
    private double windSpeed;
}
