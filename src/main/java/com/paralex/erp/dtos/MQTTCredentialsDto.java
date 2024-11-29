package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MQTTCredentialsDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String uri;

    @NotNull
    @NotEmpty
    @NotBlank
    private String username;

    @NotNull
    @NotEmpty
    @NotBlank
    private String password;

    @NotNull
    private int connectionTimeout;

    @NotNull
    private int keepAliveInterval;

    @NotNull
    private boolean automaticReconnect;

    @NotNull
    private boolean cleanStart;
}
