package com.nrs1209.travelapp.external.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchResponseDTO {

    private String originAirport;
    private String originCityName;
    private String destinationAirport;
    private String destinationCityName;
    private String departDate;
    private String returnDate;
    private int lowestPrice;
    private String airlineName;
    private String flightDuration;
    private boolean isDirect;
    private List<FlightDealItem> flightDeals;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightDealItem {

        private String airline;
        private int price;
        private String departureTime;
        private String arrivalTime;
        private boolean isDirect;
    }
}
