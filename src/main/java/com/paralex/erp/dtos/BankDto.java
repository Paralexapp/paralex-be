package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BankDto {
    private List<BankData> data;
    private Meta meta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Meta {
        @Nullable
        private String next;

        @Nullable
        private String previous;

        private long perPage;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class BankData {
        @NotNull
        private long id;

        @NotNull
        @NotEmpty
        @NotBlank
        private String name;

        @NotNull
        @NotEmpty
        @NotBlank
        private String slug;

        @NotNull
        @NotEmpty
        @NotBlank
        private String code;

        @NotNull
        @NotEmpty
        @NotBlank
        private String longCode;

        @NotNull
        private boolean payWithBank;

        @NotNull
        private boolean supportsTransfer;

        @NotNull
        private boolean active;

        @NotNull
        @NotEmpty
        @NotBlank
        private String country;

        @NotNull
        @NotEmpty
        @NotBlank
        private String currency;

        @NotNull
        @NotEmpty
        @NotBlank
        private String type;

        @NotNull
        private boolean isDeleted;

        @NotNull
        @NotEmpty
        @NotBlank
        private String createdAt;

        @NotNull
        @NotEmpty
        @NotBlank
        private String updatedAt;
    }
}
