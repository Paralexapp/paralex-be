package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "litigationSupportRequests")
public class LitigationSupportRequestEntity {
    @Id
    private String id;

    @NotNull
    @Field(value = "matterTitle")
    @Setter
    private String matterTitle;

    @NotNull
    @Field(value = "matterDescription")
    @Setter
    private String matterDescription;

    @NotNull
    @Field(value = "matterRecordingUrl")
    @Setter
    private String matterRecordingUrl;

    @NotNull
    @Field(value = "deadline")
    @Setter
    private LocalDate deadline;

    @JsonManagedReference
    @DBRef
    @Field(value = "files")
    private List<LitigationSupportRequestFileEntity> files;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "userId")
    @Setter
    private String userId;

    @DBRef
    @Field(value = "user")
    private UserEntity user;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "lawyerProfileId")
    @Setter
    private String lawyerProfileId;

    @DBRef
    @Field(value = "lawyerProfile")
    private LawyerProfileEntity lawyerProfile;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "lawFirmId")
    @Setter
    private String lawFirmId;

    @DBRef
    @Field(value = "lawFirm")
    private LawFirmEntity lawFirm;

    @Field(value = "paid")
    @Setter
    private boolean paid;

    @Field(value = "paymentRequestCode")
    @Setter
    private String paymentRequestCode;

    @Field(value = "litigationFileNumber")
    @Setter
    private String litigationFileNumber;

    @JsonManagedReference
    @DBRef
    @Field(value = "negotiations")
    @Builder.Default
    private List<LitigationSupportNegotiationEntity> negotiations = List.of();

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
