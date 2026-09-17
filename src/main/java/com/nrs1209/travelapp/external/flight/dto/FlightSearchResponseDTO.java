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

        private String id;
        private String airline;
        private String flightNumber;
        private String outboundFlightNo; // 가는편 편명
        private String inboundFlightNo; // 오는편 편명
        private int price;
        private String tag; // 뱃지 태그
        private int remainingSeats; // 남은 좌석 수
        private String baggageInfo;
        private String cabinClass;

        // 가는편
        private String outboundDeptTime;
        private String outboundArrTime;
        private String outboundDuration;
        private boolean outboundDirect;

        // 오는편
        private String inboundDeptTime;
        private String inboundArrTime;
        private String inboundDuration;
        private boolean inboundDirect;
    }
}
