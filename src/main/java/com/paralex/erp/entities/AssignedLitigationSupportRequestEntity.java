package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignedLitigationSupportRequests")
@Entity
@DynamicUpdate
@DynamicInsert
public class AssignedLitigationSupportRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "litigationSupportRequestId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String litigationSupportRequestId;

    @OneToOne
    @JoinColumn(name = "litigationSupportRequestId", insertable = false, updatable = false)
    private LitigationSupportRequestEntity litigationSupportRequest;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "lawyerProfileId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String lawyerProfileId;

    @OneToOne
    @JoinColumn(name = "lawyerProfileId", insertable = false, updatable = false)
    private LawyerProfileEntity lawyerProfile;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "accepted", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean accepted;

    @NotNull
    @Column(name = "rejected", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean rejected;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "creatorId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String creatorId;

    @OneToOne
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private UserEntity creator;

    @NotNull
    @Column(name = "status", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean status;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;
}
