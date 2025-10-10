package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    @Size(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이하로 입력해주세요.")
    private String nickname;

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;
}
