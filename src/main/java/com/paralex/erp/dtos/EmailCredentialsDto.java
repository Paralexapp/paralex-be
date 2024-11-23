package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailCredentialsDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private final String host;

    @NotNull
    private final int port;

    @NotNull
    @NotEmpty
    @NotBlank
    private final String username;

    @NotNull
    @NotEmpty
    @NotBlank
    private final String password;

    @NotNull
    @NotEmpty
    @NotBlank
    private final String protocol;

    @NotNull
    private final boolean authentication;

    @NotNull
    private final boolean tlsEnabled;

    @NotNull
    private final boolean debug;
}
