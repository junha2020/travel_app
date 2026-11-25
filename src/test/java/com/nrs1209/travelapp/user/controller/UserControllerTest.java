package com.nrs1209.travelapp.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrs1209.travelapp.global.security.JwtUtil;
import com.nrs1209.travelapp.plan.entity.Plan;
import com.nrs1209.travelapp.plan.repository.PlanRepository;
import com.nrs1209.travelapp.user.dto.UserSignUpRequestDTO;
import com.nrs1209.travelapp.user.dto.UserUpdateRequestDTO;
import com.nrs1209.travelapp.user.entity.User;
import com.nrs1209.travelapp.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        // 1단계: 토큰 없이 삭제 시도 -> 401 에러
        mockMvc.perform(delete("/api/plans/" + victimPlan.getId()))
                .andExpect(status().isUnauthorized());

        // 2단계: 해커 토큰으로 남의 일정 삭제(BOLA) -> validatePlanOwnership에서 403 에러
        mockMvc.perform(delete("/api/plans/" + victimPlan.getId())
                .header("Authorization", "Bearer " + attackerToken))
                .andExpect(status().isForbidden());

        // 3단계: 진짜 주인이 삭제 -> 204
        mockMvc.perform(delete("/api/plans/" + victimPlan.getId())
                .header("Authorization", "Bearer " + victimToken))
                .andExpect(status().isNoContent());
    }

    // 사진 프록시 정규식 화이트리스트 검증 및 비밀번호 변경 성공/실패 테스트
    @Test
    @DisplayName("보안 테스트 - 사진 프록시 악성 경로(Path Traversal) 입력 시 400 차단")
    public void testPlacePhoto_InvalidPathBlocked() throws Exception {
        mockMvc.perform(get("/api/places/photo")
                .param("name", "places/../../hack?key=evil"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("올바르지 않은 사진 경로입니다."));
    }

    @Test
    @DisplayName("기능/보안 테스트 - 비밀번호 변경 시 현재 비번 불일치 400 차단 및 일치 시 200 성공")
    public void testChangePassword_ValidationAndSuccess() throws Exception {
        // given: BCrypt로 암호화된 유저 저장 및 토큰 발급
        User user = userRepository.save(User.builder()
                .userName("pw_test_user")
                .password(passwordEncoder.encode("oldpassword123"))
                .email("pw_test@test.com")
                .nickName("비번테스터")
                .build());

        String token = jwtUtil.generateToken(user.getUserName());

        // 1단계: 현재 비밀번호를 틀리게 입력 -> 400 Bad Request + JSON 메시지 확인
        UserUpdateRequestDTO wrongDto = new UserUpdateRequestDTO();
        wrongDto.setCurrentPassword("wrongpassword");
        wrongDto.setNewPassword("newpassword123");

        mockMvc.perform(put("/api/users/" + user.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("현재 비밀번호가 일치하지 않습니다."));

        // 2단계: 현재 비밀번호 일치 + 새 비밀번호 (8자 이상) 입력 -> 200 OK 성공!
        UserUpdateRequestDTO validDto = new UserUpdateRequestDTO();
        validDto.setCurrentPassword("oldpassword123");
        validDto.setNewPassword("newpassword123");

        mockMvc.perform(put("/api/users/" + user.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());
    }
}
