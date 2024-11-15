package com.kosa.mini.api.dto.member;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class EmailFindRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String phoneNumber;
}
