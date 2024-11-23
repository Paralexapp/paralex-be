package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto {
    private String resetToken;
    private String newPassword;
    private String confirmPassword;
}
