package com.kosa.mini.api.dto.member;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String name;
    private String phoneNumber;
    private String email;
    private String newPassword;
    private String confirmPassword;
}
