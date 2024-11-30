package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courts")
public class CourtEntity {

    @Id
    private String id;  // Mongo uses String for id instead of UUID

    @NotNull
    @Field("location")
    private String location;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("courtDivisionId")
    private String courtDivisionId;

    @Field("courtDivision")
    private CourtDivisionEntity courtDivision; // In MongoDB, this can be embedded as a document

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("creatorId")
    private String creatorId;

    @Field("creator")
    private UserEntity creator; // Embedded document or reference, depending on application design

    @NotNull
    @Field("status")
    private boolean status;

    @Field("time")
    private LocalDateTime time;
}