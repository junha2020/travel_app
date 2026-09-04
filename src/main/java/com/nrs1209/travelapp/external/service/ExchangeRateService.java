package com.nrs1209.travelapp.external.service;

import com.nrs1209.travelapp.external.dto.ExchangeRateResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private static final String CURRENCY_API_URL = "https://open.er-api.com/v6/latest/JPY";

    @SuppressWarnings("unchecked")
    public ExchangeRateResponseDTO getJpyToKrwRate() {
        try {
            Map<String, Object> response = restTemplate.getForObject(CURRENCY_API_URL, Map.class);

            if (response != null && "success".equals(response.get("result"))) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                if (rates != null && rates.containsKey("KRW")) {
                    double krwRate = Double.parseDouble(rates.get("KRW").toString());
                    double rateFor100 = Math.round(krwRate * 100.0 * 100.0) / 100.0;

                    return ExchangeRateResponseDTO.builder()
                            .baseCurrency("JPY")
                            .targetCurrency("KRW")
                            .rate(krwRate)
                            .rateFor100Yen(rateFor100)
                            .updatedAt(LocalDateTime.now())
                            .build();
                }
            }
        } catch (Exception e) {
            log.error("실시간 환율 조회 실패: {}", e.getMessage());
        }

        // 네트워크 장애 시 안전 fallback
        return ExchangeRateResponseDTO.builder()
                .baseCurrency("JPY")
                .targetCurrency("KRW")
                .rate(10.00)
                .rateFor100Yen(1000.00)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
