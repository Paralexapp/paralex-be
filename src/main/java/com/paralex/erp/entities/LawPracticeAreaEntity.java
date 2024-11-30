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
@Document(collection = "lawPracticeAreas")
public class LawPracticeAreaEntity {
    @Id
    private String id;

    @NotNull
    @Field(value = "name", write = Field.Write.NON_NULL)
    @Setter
    private String name;

    @NotNull
    @Field(value = "description", write = Field.Write.NON_NULL)
    @Setter
    private String description;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "creator", write = Field.Write.NON_NULL)
    private UserEntity creator; // Assuming UserEntity is mapped as a MongoDB document

    @NotNull
    @Field(value = "status", write = Field.Write.NON_NULL)
    @Setter
    private boolean status;

    @Field(value = "time", write = Field.Write.NON_NULL)
    private LocalDateTime time;
}
