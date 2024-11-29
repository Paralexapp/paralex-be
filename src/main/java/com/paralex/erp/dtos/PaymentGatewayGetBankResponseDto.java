package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PaymentGatewayGetBankResponseDto {
    private List<BankDto.BankData> data;
    private BankDto.Meta meta;

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
        private long id;
        private String name;
        private String slug;
        private String code;

        @JsonProperty("long_code")
        private String longCode;

        @JsonProperty("pay_with_bank")
        private boolean payWithBank;

        @JsonProperty("supports_transfer")
        private boolean supportsTransfer;

        private boolean active;
        private String country;
        private String currency;
        private String type;

        @JsonProperty("is_deleted")
        private boolean isDeleted;

        private String createdAt;
        private String updatedAt;
    }
}
