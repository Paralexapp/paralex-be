package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserVerificationResponseDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String photoUrl;

    @NotNull
    @NotEmpty
    @NotBlank
    private String stateOfResidence;

    @NotNull
    @NotEmpty
    @NotBlank
    private String sessionToken;

    @NotNull
    @NotEmpty
    @NotBlank
    private String idToken;

    @NotNull
    private LocalDateTime time;
}
