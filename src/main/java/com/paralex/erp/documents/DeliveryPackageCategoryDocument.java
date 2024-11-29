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
@Document("deliveryPackageCategories")
public class DeliveryPackageCategoryDocument {
    @Id
    private String id;

    @Field(value = "name", write = Field.Write.NON_NULL)
    @Setter
    private String name;

    @Field(value = "description", write = Field.Write.NON_NULL)
    @Setter
    private String description;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "status", write = Field.Write.NON_NULL)
    private boolean status;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
