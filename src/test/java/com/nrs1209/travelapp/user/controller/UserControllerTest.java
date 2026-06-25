package com.nrs1209.travelapp.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrs1209.travelapp.user.dto.UserSignUpRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @DisplayName("회원가입 테스트 - 성공")
    public void testRegisterUser() throws Exception {
        // given
        UserSignUpRequestDTO signUpRequestDTO = UserSignUpRequestDTO.builder()
                .userName("test")
                .password("testtest")
                .email("test@test.com")
                .nickName("test")
                .build();

        // when & then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDTO)))
                .andExpect(status().isCreated());
    }
}
