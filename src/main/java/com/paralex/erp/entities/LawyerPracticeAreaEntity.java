package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Document(collection = "lawyerPracticeAreas")
public class LawyerPracticeAreaEntity {
    @Id
    private String id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "lawPracticeAreaId", write = Field.Write.NON_NULL)
    @Setter
    private String lawPracticeAreaId;

    @Field(value = "lawPracticeArea", write = Field.Write.NON_NULL)
    private LawPracticeAreaEntity lawPracticeArea; // Assuming LawPracticeAreaEntity is a MongoDB document

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "lawyerProfileId", write = Field.Write.NON_NULL)
    @Setter
    private String lawyerProfileId;

    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "lawyerProfile", write = Field.Write.NON_NULL)
    private LawyerProfileEntity lawyerProfile; // Assuming LawyerProfileEntity is a MongoDB document

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @Setter
    private String creatorId;

    @Field(value = "creator", write = Field.Write.NON_NULL)
    private UserEntity creator; // Assuming UserEntity is a MongoDB document

    @Field(value = "time", write = Field.Write.NON_NULL)
    private LocalDateTime time;
}
