package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lawFirms")
public class LawFirmEntity {
    @Id
    private String id;

    @Field(value = "name", write = Field.Write.NON_NULL)
    @Setter
    private String name;

    @Field(value = "location", write = Field.Write.NON_NULL)
    @Setter
    private Point location;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String creatorId;

    @Field(value = "creator", write = Field.Write.NON_NULL)
    @Setter
    private UserEntity creator; // Assuming UserEntity is mapped to a MongoDB document

    @Field(value = "status", write = Field.Write.NON_NULL)
    @Setter
    private boolean status;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
