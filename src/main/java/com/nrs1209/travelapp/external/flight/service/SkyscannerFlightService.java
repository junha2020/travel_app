package com.nrs1209.travelapp.external.flight.service;

import com.nrs1209.travelapp.external.flight.dto.FlightSearchResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkyscannerFlightService {

    private final RestTemplate restTemplate;

    @Value("${rapidapi.key:}")
    private String rapidApiKey;

    private static final String RAPID_API_HOST = "skyscanner-flights4.p.rapidapi.com";

    /**
     * 실시간 왕복 항공권 최저가 검색
     */
    public FlightSearchResponseDTO searchFlights(String origin, String destination, String departDate, String returnDate) {
        String originCode = normaizeAirportCode(origin, "ICN");
        String destCode = mapCityToAirportCode(destination);
        String originName = mapAirportToCityName(originCode);
        String destName = mapAirportToCityNmae(destCode);

        //　RapidAPI 키가 있으면 실시간 호출 시도
        if (rapidApiKey != null && !rapidApiKey.isBlank()) {
            try {
                String formattedDepart = (departDate != null && !departDate.isBlank()) ? departDate.replace(".", "-") : "2026-09-20";
                String formattedReturn = (returnDate != null && !returnDate.isBlank()) ? returnDate.replace(".", "-") : "2026-09-24";

                String url = String.format(
                        "https://%s/search?adults=1&origin=%s&destination=%s&departureDate=%s&returnDate=%s&currency=KRW",
                        RAPID_API_HOST, originCode, destCode, formattedDepart, formattedReturn
                );

                HttpHeaders headers = new HttpHeaders();
                headers.set("x-rapidapi-key", rapidApiKey);
                headers.set("x-rapidapi-host", RAPID_API_HOST);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    log.info("스카이스캐너 실시간 항공권 조회 성공! [{} ➔ {}]", originCode, destCode);
                    // 실시간 응답 파싱 필요한 경우 여기서 세부 매핑
                }
            } catch (Exception e) {
                log.warn("RapidAPI 항공원 호출 실패: {}", e.getMessage());
            }
        }

        return createFallbackFlightDeal(originCode, originName, destCode, destName, departDate, returnDate);
    }

    /**
     * API JSON 응답 안전하게 파싱
     */
    private FlightSearchResponseDTO parseApiResponse(
            Map<?, ?> body, String originCode, String originName, String destCode, String destName, String depart, String ret) {
        try {
            // 최저가 추출 로직
            if (body.containsKey("data")) {
                return createFallbackFlightDeal(originCode, originName, destCode, destName, depart, ret);
            }
        } catch (Exception e) {
            log.warn("API 파싱 중 에러: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 도시명 또는 입력값을 IATA 코드로 변환
     * 김포
     */
}
