package com.nrs1209.travelapp.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDTO {

    private String baseCurrency;
    private String targetCurrency;
    private double rate;
    private double rateFor100Yen;
    private LocalDateTime updatedAt;
}
