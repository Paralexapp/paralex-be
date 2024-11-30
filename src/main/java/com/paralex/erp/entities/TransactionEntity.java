package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
@Builder
public class TransactionEntity {
    @Id
    private String id;

    private String name;

    private int amount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String transactionItemId;

    @JsonBackReference
    @DBRef
    private TransactionItemEntity transactionItem;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String creatorId;

    @DBRef
    private UserEntity creator;

    private boolean status;

    private LocalDateTime time;
}
