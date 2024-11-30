package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locations")
public class LocationEntity {
    @Id
    private String id;

    @NotNull
    @Field(value = "name")
    @Setter
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "status")
    @Setter
    private boolean status;

    @NotNull
    @Field(value = "location")
    @Setter
    private Point location;

    // INFO: price per kilometer for delivery location pricing
    @NotNull
    @Field(value = "amount")
    @Setter
    private int amount;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "creatorId")
    @Setter
    private String creatorId;

    @DBRef
    @Field(value = "creator")
    private UserEntity creator;

    @Field(value = "time")
    private LocalDateTime time;
}
