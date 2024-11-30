package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assignedLitigationSupportRequests")
public class AssignedLitigationSupportRequestEntity {

    @Id
    private String id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("litigationSupportRequestId")
    private String litigationSupportRequestId;

    @DBRef
    private LitigationSupportRequestEntity litigationSupportRequest;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("lawyerProfileId")
    private String lawyerProfileId;

    @DBRef
    private LawyerProfileEntity lawyerProfile;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("accepted")
    private boolean accepted;

    @NotNull
    @Field("rejected")
    private boolean rejected;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("creatorId")
    private String creatorId;

    @DBRef
    private UserEntity creator;

    @NotNull
    @Field("status")
    private boolean status;

    @Field("time")
    private LocalDateTime time;
}
