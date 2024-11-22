package com.paralex.erp.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("authorizationRecords")
public class AuthorizationRecordDocument {
    @Id
    private String id;

    @Field(value = "resource", write = Field.Write.NON_NULL)
    @Setter
    private String resource;

    @Field(value = "status", write = Field.Write.NON_NULL)
    @Setter
    private String status;

    @Field(value = "action", write = Field.Write.NON_NULL)
    @Setter
    private String action;

    @Field(value = "principal", write = Field.Write.NON_NULL)
    @Setter
    private String principal;

    @Field(value = "targets", write = Field.Write.NON_NULL)
    @Setter
    private List<String> targets;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
