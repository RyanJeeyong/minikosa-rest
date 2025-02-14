package com.kosa.mini.api.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDTO {
    private Integer memberId;
    private String name;
    private String email;
    private String nickname;
    private Integer roleId;
    private String accessToken;
    private String refreshToken;
}
