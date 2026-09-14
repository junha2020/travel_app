package com.nrs1209.travelapp.external.controller;

import com.nrs1209.travelapp.external.currency.dto.ExchangeRateResponseDTO;
import com.nrs1209.travelapp.external.flight.dto.FlightSearchResponseDTO;
import com.nrs1209.travelapp.external.flight.service.SkyscannerFlightService;
import com.nrs1209.travelapp.external.weather.dto.WeatherResponseDTO;
import com.nrs1209.travelapp.external.currency.service.ExchangeRateService;
import com.nrs1209.travelapp.external.weather.service.WeatherService;
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
    private final SkyscannerFlightService skyscannerFlightService;

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

    @GetMapping("/flights")
    public ResponseEntity<FlightSearchResponseDTO> getFlightDeals(
            @RequestParam(defaultValue = "ICN") String origin,
            @RequestParam(defaultValue = "TYO") String destination,
            @RequestParam(required = false, defaultValue = "2026-10-26") String departDate,
            @RequestParam(required = false, defaultValue = "2026-10-29") String returnDate) {
        FlightSearchResponseDTO response = skyscannerFlightService.searchFlights(origin, destination, departDate, returnDate);
        return ResponseEntity.ok(response);
    }
}
