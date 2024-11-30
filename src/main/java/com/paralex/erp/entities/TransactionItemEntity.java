package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "transactionItems")
public class TransactionItemEntity {
    @Id
    private String id;

    private String name;

    @JsonManagedReference
    @DBRef
    private List<TransactionEntity> transactions;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String creatorId;

    @DBRef
    private UserEntity creator;

    private boolean status;

    private LocalDateTime time;
}
