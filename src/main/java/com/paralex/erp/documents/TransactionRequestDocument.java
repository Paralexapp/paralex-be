package com.paralex.erp.documents;

import com.paralex.erp.entities.TransactionEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("transactionRequests")
public class TransactionRequestDocument {
    @Id
    private String id;

    @Field(value = "transactionReference", write = Field.Write.NON_NULL)
    @Setter
    private String transactionReference;

    @Field(value = "processed")
    @Setter
    @Builder.Default
    private boolean processed = false;

    @Field(value = "suspended", write = Field.Write.NON_NULL)
    @Setter
    @Builder.Default
    private boolean suspended = false;

    @Field(value = "transaction", write = Field.Write.NON_NULL)
    @Setter
    private TransactionEntity transaction;

    @Field(value = "amountPaid", write = Field.Write.NON_NULL)
    @Setter
    private long amountPaid;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
