package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "reset_request")
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetRequest {

    @Id
    private String id;  // MongoDB uses String as default for ID, instead of Long

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private String customerId;
}
