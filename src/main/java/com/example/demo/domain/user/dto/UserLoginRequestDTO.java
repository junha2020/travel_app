package com.example.demo.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDTO {

    @NotBlank(message = "아이디를 입력하십시오.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하십시오.")
    private String password;

}
