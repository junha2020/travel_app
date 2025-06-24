package com.example.demo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDTO {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 4, max = 15, message = "아이디는 4~15자여야 합니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이여야 합니다")
    private String password;

    @NotBlank(message = "유효한 이메일을 입력하십시오.")
    @Email(message = "유효한 이메일 값이 아닙니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

}
