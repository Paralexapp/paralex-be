package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lawyerProfiles")
public class LawyerProfileEntity {
    @Id
    private String id;

    @NotNull
    @Field(value = "state")
    @Setter
    private String state;

    @NotNull
    @Field(value = "location")
    @Setter
    private Point location;

    @NotNull
    @Field(value = "supremeCourtNumber")
    @Setter
    private String supremeCourtNumber;

    @JsonManagedReference
//    @DBRef
    @Field(value = "practiceAreas")
    private List<String> practiceAreas;

    private String aboutLawyer;

    @Field("averageRating")
    private double averageRating;

    @Field("totalReviews")
    private int totalReviews;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "userId")
    @Setter
    private String userId;

    @DBRef
    @Field(value = "user")
    private UserEntity user; // Assuming UserEntity is a MongoDB document

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "creatorId")
    @Setter
    private String creatorId;

    private String lawyerName;

    @DBRef
    @Field(value = "creator")
    @JsonIgnore
    private UserEntity creator; // Assuming UserEntity is a MongoDB document

    @NotNull
    @Field(value = "status")
    @Setter
    private boolean status;

    @Field(value = "time")
    private LocalDateTime time;
}
