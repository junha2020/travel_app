package com.nrs1209.travelapp.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrs1209.travelapp.global.security.JwtUtil;
import com.nrs1209.travelapp.plan.entity.Plan;
import com.nrs1209.travelapp.plan.repository.PlanRepository;
import com.nrs1209.travelapp.user.dto.UserSignUpRequestDTO;
import com.nrs1209.travelapp.user.entity.User;
import com.nrs1209.travelapp.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PlanRepository planRepository;

    @Test
    @DisplayName("회원가입 테스트 - 성공")
    public void testRegisterUser() throws Exception {
        // given
        UserSignUpRequestDTO signUpRequestDTO = UserSignUpRequestDTO.builder()
                .userName("junit_test_user")
                .password("testtest")
                .email("junit_test@test.com")
                .nickName("테스트유저")
                .build();

        // when & then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("보안 테스트 - 타인의 회원 정보 조회 시 403 Forbidden 차단 (BOLA 방어)")
    public void testGetMe_BolaForbidden() throws Exception {
        // given: 피해자(victim) 계정 저장
        User victim = userRepository.save(User.builder()
                .userName("victim_user")
                .password("encoded_pw")
                .email("victim@test.com")
                .nickName("피해자")
                .build());

        // 해커 명의 JWT 토큰 발급
        String attackerToken = jwtUtil.generateToken("attacker_user");

        // when & then: 해커 토큰으로 피해자의 userId를 조회 시도 -> 403 Forbidden 기대
        mockMvc.perform(get("/api/users/me")
                .param("userId", String.valueOf(victim.getId()))
                .header("Authorization", "Bearer " + attackerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("보안 테스트 - 여행 일정(Plan) 무토큰 차단, 타인 삭제 BOLA 403 차단, 본인 삭제 204 성공")
    public void testDeletePlan_SecurityAndBola() throws Exception {
        //given1: 피해자(victim)와 해커 (attacker) 둘 다 DB에 생성
        User victim = userRepository.save(User.builder()
                .userName("plan_victim")
                .password("encoded_pw")
                .email("plan_victim@test.com")
                .nickName("일정주인")
                .build());

        User attacker = userRepository.save(User.builder()
                .userName("plan_attacker")
                .password("encoded_pw")
                .email("plan_attacker@test.com")
                .nickName("해커")
                .build());

        // given2: 피해자(victim) 소유의 여행 일정 생성
        Plan victimPlan = planRepository.save(Plan.builder()
                .title("제주도 여행")
                .startDate(LocalDate.of(2026, 12, 1))
                .endDate(LocalDate.of(2026, 12, 5))
                .userId(victim.getId())
                .isPublic(false)
                .build());

        String attackerToken = jwtUtil.generateToken(attacker.getUserName());
        String victimToken = jwtUtil.generateToken(victim.getUserName());

        // 1단계: 토큰 없이 삭제 시도 -> 403 에러
        mockMvc.perform(delete("/api/plans/" + victimPlan.getId()))
                .andExpect(status().isForbidden());

        // 2단계: 해커 토큰으로 남의 일정 삭제(BOLA) -> validatePlanOwnership에서 403 에러
        mockMvc.perform(delete("/api/plans/" + victimPlan.getId())
                .header("Authorization", "Bearer " + attackerToken))
                .andExpect(status().isForbidden());

        // 3단계: 진짜 주인이 삭제 -> 204
        mockMvc.perform(delete("/api/plans/" + victimPlan.getId())
                .header("Authorization", "Bearer " + victimToken))
                .andExpect(status().isNoContent());
    }
}
