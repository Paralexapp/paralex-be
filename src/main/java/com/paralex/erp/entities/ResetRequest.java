package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "requestData")
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetRequest {
    @Id
    private String id;

    @Field("reset_token")
    private String resetToken;

    @Field("reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    @Field("customer_id")
    private String customerId;
}
