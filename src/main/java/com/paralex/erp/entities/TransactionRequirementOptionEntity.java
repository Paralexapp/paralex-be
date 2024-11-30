package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "transactionRequirementOptions")
public class TransactionRequirementOptionEntity {
    @Id
    private String id;

    private String label;

    private String data;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String transactionRequirementId;

    @DBRef
    private TransactionRequirementEntity transactionRequirement;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String creatorId;

    @DBRef
    private UserEntity creator;

    private boolean status;

    private LocalDateTime time;
}
