package com.nrs1209.travelapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordResponseDTO {

    private String message;
    private String tempPassword; // 프론트렌드 테스트 편의를 위해 발급된 비밀번호 동봉
}
