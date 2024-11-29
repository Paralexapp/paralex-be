package com.paralex.erp.documents;

import com.paralex.erp.dtos.DeliveryRequestDestinationDto;
import com.paralex.erp.dtos.DeliveryRequestPickupDto;
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
@Document("deliveryRequests")
public class DeliveryRequestDocument {
    @Id
    private String id;

    @Field(value = "trackingId", write = Field.Write.NON_NULL)
    @Setter
    private String trackingId;

    @Field(value = "deliveryStageId", write = Field.Write.NON_NULL)
    @Setter
    private String deliveryStageId;

    @Field(value = "driverProfileId", write = Field.Write.NON_NULL)
    @Setter
    private String driverProfileId;

    @Field(value = "pickup", write = Field.Write.NON_NULL)
    @Setter
    private DeliveryRequestPickupDto pickup;

    @Field(value = "destination", write = Field.Write.NON_NULL)
    @Setter
    private DeliveryRequestDestinationDto destination;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
