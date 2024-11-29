package com.paralex.erp.documents;

import com.paralex.erp.dtos.VerifyTransactionByReferenceResponseDto;
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
@Document("paymentHistories")
public class PaymentHistoryDocument {
    @Id
    private String id;

    @Field(value = "domain", write = Field.Write.NON_NULL)
    @Setter
    public String domain;

    @Field(value = "status", write = Field.Write.NON_NULL)
    @Setter
    public String status;

    @Field(value = "transactionId", write = Field.Write.NON_NULL)
    @Setter
    private long transactionId;

    @Field(value = "transactionReference", write = Field.Write.NON_NULL)
    @Setter
    private String transactionReference;

    @Field(value = "receiptNumber", write = Field.Write.NON_NULL)
    @Setter
    public Object receiptNumber;

    @Field(value = "amount", write = Field.Write.NON_NULL)
    @Setter
    public int amount;

    @Field(value = "channel", write = Field.Write.NON_NULL)
    @Setter
    public String channel;

    @Field(value = "gatewayResponse", write = Field.Write.NON_NULL)
    @Setter
    private VerifyTransactionByReferenceResponseDto gatewayResponse;

    @Field(value = "target", write = Field.Write.NON_NULL)
    @Setter
    private String target;

    @Field(value = "targetId", write = Field.Write.NON_NULL)
    @Setter
    private String targetId;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
