package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class inAppPasswordResetDTO {
    @NotNull(message = "Please provide an old password")
    private String oldPassword;

    @NotNull(message = "Please provide a new password equal to the confirm password")
    private String newPassword;

    @NotNull(message = "Please provide a confirm password")
    private String confirmPassword;
}
