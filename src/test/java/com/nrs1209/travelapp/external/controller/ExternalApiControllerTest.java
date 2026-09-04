package com.nrs1209.travelapp.external.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExternalApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("1. 실시간 엔화 환율 조회 API (/api/external/currency) - 성공 (200 OK)")
    public void textGetJpyExchangeRate() throws Exception {
        mockMvc.perform(get("/api/external/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseCurrency").value("JPY"))
                .andExpect(jsonPath("$.targetCurrency").value("KRW"))
                .andExpect(jsonPath("$.rate").isNumber())
                .andExpect(jsonPath("$.rateFor100Yen").isNumber())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("2. 도쿄 실시간 날씨 조회 API (/api/external/weather?cityName=도쿄) - 성공 (200 OK)")
    public void testGetTokyoWeather() throws Exception {
        mockMvc.perform(get("/api/external/weather")
                        .param("cityName", "도쿄")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("도쿄"))
                .andExpect(jsonPath("$.temperature").isNumber())
                .andExpect(jsonPath("$.humidity").isNumber())
                .andExpect(jsonPath("$.weatherText").isNotEmpty())
                .andExpect(jsonPath("$.weatherEmoji").isNotEmpty())
                .andExpect(jsonPath("$.windSpeed").isNumber());
    }

    @Test
    @DisplayName("2. 도쿄 실시간 날씨 조회 API (/api/external/weather?cityName=도쿄) - 성공 (200 OK)")
    public void testGetOsakaWeather() throws Exception {
        mockMvc.perform(get("/api/external/weather")
                        .param("cityName", "오사카")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("오사카"))
                .andExpect(jsonPath("$.temperature").isNumber())
                .andExpect(jsonPath("$.humidity").isNumber())
                .andExpect(jsonPath("$.weatherText").isNotEmpty())
                .andExpect(jsonPath("$.weatherEmoji").isNotEmpty())
                .andExpect(jsonPath("$.windSpeed").isNumber());
    }

    @Test
    @DisplayName("2. 도쿄 실시간 날씨 조회 API (/api/external/weather?cityName=도쿄) - 성공 (200 OK)")
    public void testGetDefaultWeather() throws Exception {
        mockMvc.perform(get("/api/external/weather")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("도쿄"));
    }
}
