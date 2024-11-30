package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
@Document(collection = "courtDivisions")
public class CourtDivisionEntity {

    @Id
    private String id;  // Mongo uses String for id instead of UUID

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("creatorId")
    private String creatorId;

    @Field("creator")
    private UserEntity creator; // In MongoDB, the creator will be stored as an embedded document if required.

    @NotNull
    @Field("status")
    private boolean status;

    @Field("time")
    private LocalDateTime time;
}