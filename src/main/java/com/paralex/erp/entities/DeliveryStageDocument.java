package com.paralex.erp.entities;

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
@Document("deliveryStages")
public class DeliveryStageDocument {
    @Id
    private String id;

    @Field(value = "name", write = Field.Write.NON_NULL)
    @Setter
    private String name;

    @Field(value = "initial", write = Field.Write.NON_NULL)
    @Setter
    private boolean initial;

    @Field(value = "terminal", write = Field.Write.NON_NULL)
    @Setter
    private boolean terminal;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "status", write = Field.Write.NON_NULL)
    @Setter
    private boolean status;

    @Field(value = "forDriver", write = Field.Write.NON_NULL)
    @Setter
    private boolean forDriver;

    @Field(value = "forAdmin", write = Field.Write.NON_NULL)
    @Setter
    private boolean forAdmin;

    @Field(value = "shouldNotify", write = Field.Write.NON_NULL)
    @Setter
    private boolean shouldNotify;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
