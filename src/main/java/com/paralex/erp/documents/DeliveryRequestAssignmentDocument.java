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
@Document("deliveryRequestAssignments")
public class DeliveryRequestAssignmentDocument {
    @Id
    private String id;

    @Field(value = "accepted", write = Field.Write.NON_NULL)
    @Setter
    private boolean accepted;

    @Field(value = "declined", write = Field.Write.NON_NULL)
    @Setter
    private boolean declined;

    @Field(value = "deliveryRequestId", write = Field.Write.NON_NULL)
    @Setter
    private String deliveryRequestId;

    @Field(value = "driverUserId", write = Field.Write.NON_NULL)
    @Setter
    private String driverUserId;

    @Field(value = "driverProfileId", write = Field.Write.NON_NULL)
    @Setter
    private String driverProfileId;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
