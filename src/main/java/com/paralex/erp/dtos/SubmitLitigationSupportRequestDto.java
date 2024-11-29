package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SubmitLitigationSupportRequestDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String matterTitle;

    @NotNull
    @NotEmpty
    @NotBlank
    private String matterDescription;

    @NotNull
    @NotEmpty
    @NotBlank
    private String matterRecordingUrl;

    @NotNull
    private LocalDate deadline;

    @NotNull
    private List<AddFileDto> files = List.of();

    @Nullable
    private String lawyerProfileId;

    @Nullable
    private String lawFirmId;

    @Nullable
    private String userId;

    // INFO 2 billion is a little too much
    @NotNull
    private int amount;
}
