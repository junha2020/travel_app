package com.nrs1209.travelapp.external.service;

import com.nrs1209.travelapp.external.dto.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public WeatherResponseDTO getCurrentWeather(String cityName) {
        String targetCity = (cityName == null || cityName.isBlank()) ? "도쿄" : cityName;
        double[] coords = getCityCoordinates(targetCity);

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Asia%%2FTokyo",
                coords[0], coords[1]
        );

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("current")) {
                Map<String, Object> current = (Map<String, Object>) response.get("current");

                double temp = Double.parseDouble(current.get("temperature_2m").toString());
                int humidity = Integer.parseInt(current.get("relative_humidity_2m").toString());
                int weatherCode = Integer.parseInt(current.get("weather_code").toString());
                double windSpeed = Double.parseDouble(current.get("wind_speed_10m").toString());

                return WeatherResponseDTO.builder()
                        .cityName(targetCity)
                        .temperature(temp)
                        .humidity(humidity)
                        .weatherText(getWeatherText(weatherCode))
                        .weatherEmoji(getWeatherEmoji(weatherCode))
                        .windSpeed(windSpeed)
                        .build();
            }
        } catch (Exception e) {
            log.error("{} 실시간 날씨 조회 실패: {}", targetCity, e.getMessage());
        }

        // 장애 시 안전 fallback
        return WeatherResponseDTO.builder()
                .cityName(targetCity)
                .temperature(22.0)
                .humidity(60)
                .weatherText("맑음")
                .weatherEmoji("☀️")
                .windSpeed(2.0)
                .build();
    }

    private double[] getCityCoordinates(String cityName) {
        return switch (cityName) {
            case "오사카" -> new double[]{34.6937, 135.5023};
            case "후쿠오카" -> new double[]{33.5902, 130.4017};
            case "삿포로" -> new double[]{43.0618, 141.3545};
            case "나고야" -> new double[]{35.1815, 136.9066};
            case "오키나와" -> new double[]{26.2124, 127.6809};
            case "다카마쓰" -> new double[]{34.3428, 134.0466};
            case "마쓰야마" -> new double[]{33.8392, 132.7656};
            default -> new double[]{35.6895, 139.6917};
        };
    }

    private String getWeatherText(int code) {
        if (code == 0) return "맑음";
        if (code >= 1 && code <=3) return "구름 조금";
        if (code >= 45 && code <=48) return "안개";
        if (code >= 51 && code <=67) return "비";
        if (code >= 71 && code <= 77) return "눈";
        if (code >= 80 && code <=82) return "소나기";
        if (code >= 95) return "뇌우";
        return "흐림";
    }

    private String getWeatherEmoji(int code) {
        if (code == 0) return "☀️";
        if (code >= 1 && code <=3) return "🌤️";
        if (code >= 45 && code <=48) return "🌫️";
        if (code >= 51 && code <=67) return "🌧️";
        if (code >= 71 && code <= 77) return "❄️";
        if (code >= 80 && code <=82) return "🌦️";
        if (code >= 95) return "⛈️";
        return "☁️";
    }
}
