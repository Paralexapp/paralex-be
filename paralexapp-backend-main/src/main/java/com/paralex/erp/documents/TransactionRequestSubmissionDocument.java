package com.paralex.erp.documents;

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
@Document("transactionRequestSubmissions")
public class TransactionRequestSubmissionDocument {
    @Id
    private String id;

    @Field(value = "value")
    @Setter
    private Object value;

    @Field(value = "label", write = Field.Write.NON_NULL)
    @Setter
    private String label;

    @Field(value = "transactionRequirementId", write = Field.Write.NON_NULL)
    @Setter
    private String transactionRequirementId;

    @Field(value = "optionId")
    @Setter
    private String optionId;

    @Field(value = "optionLabel")
    @Setter
    private String optionLabel;

    @Field(value = "transactionRequestId", write = Field.Write.NON_NULL)
    @Setter
    private String transactionRequestId;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
