package com.paralex.erp.dtos;

import com.paralex.erp.documents.TransactionRequestSubmissionDocument;
import com.paralex.erp.entities.TransactionEntity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionRequestDto {
    @Id
    @NotNull
    @NotEmpty
    @NotBlank
    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionReference;

    @NotNull
    private boolean processed = false;

    @NotNull
    private boolean suspended = false;

    @NotNull
    private TransactionEntity transaction;

    @NotNull
    private long amountPaid;

    @NotNull
    @NotEmpty
    private List<TransactionRequestSubmissionDocument> submissions;

    @NotNull
    @NotEmpty
    @NotBlank
    private String creatorId;

    @NotNull
    private LocalDateTime time;
}
