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
@Document(collection = "lawFirmPracticeAreas")
public class LawFirmPracticeAreaEntity {
    @Id
    private String id;

    @Field(value = "lawPracticeAreaId", write = Field.Write.NON_NULL)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String lawPracticeAreaId;

    @Field(value = "lawPracticeArea", write = Field.Write.NON_NULL)
    @Setter
    private LawPracticeAreaEntity lawPracticeArea; // Assuming LawPracticeAreaEntity is mapped as a MongoDB document

    @Field(value = "lawFirmId", write = Field.Write.NON_NULL)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String lawFirmId;

    @Field(value = "lawFirm", write = Field.Write.NON_NULL)
    @Setter
    private LawFirmEntity lawFirm; // Assuming LawFirmEntity is mapped as a MongoDB document

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String creatorId;

    @Field(value = "creator", write = Field.Write.NON_NULL)
    @Setter
    private UserEntity creator; // Assuming UserEntity is mapped as a MongoDB document

    @Field(value = "time", write = Field.Write.NON_NULL)
    private LocalDateTime time;
}
