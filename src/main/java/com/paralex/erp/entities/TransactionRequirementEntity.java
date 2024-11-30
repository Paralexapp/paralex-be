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
@Document(collection = "transactionRequirements")
public class TransactionRequirementEntity {
    @Id
    private String id;

    private String label;

    private String errorMessage;

    private String description;

    private int index;

    private int step;

    private String type;

    private boolean required;

    private boolean hasOption;

    private boolean multiple;

    private int max;

    private int min;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String transactionId;

    @DBRef
    private TransactionEntity transaction;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String creatorId;

    @DBRef
    private UserEntity creator;

    private boolean status;

    private LocalDateTime time;
}
