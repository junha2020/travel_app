package com.nrs1209.travelapp.external.controller;

import com.nrs1209.travelapp.external.dto.ExchangeRateResponseDTO;
import com.nrs1209.travelapp.external.dto.WeatherResponseDTO;
import com.nrs1209.travelapp.external.service.ExchangeRateService;
import com.nrs1209.travelapp.external.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ExternalApiController {

    private final ExchangeRateService exchangeRateService;
    private final WeatherService weatherService;

    @GetMapping("/currency")
    public ResponseEntity<ExchangeRateResponseDTO> getJpyExchangeRate() {
        ExchangeRateResponseDTO response = exchangeRateService.getJpyToKrwRate();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponseDTO> getWeather(
            @RequestParam(required = false, defaultValue = "도쿄") String cityName) {
        WeatherResponseDTO response = weatherService.getCurrentWeather(cityName);
        return ResponseEntity.ok(response);
    }
}
