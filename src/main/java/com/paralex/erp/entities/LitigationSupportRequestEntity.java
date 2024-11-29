package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "litigationSupportRequests")
@Entity
@DynamicUpdate
@DynamicInsert
public class LitigationSupportRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "matterTitle", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String matterTitle;

    @Column(name = "matterDescription", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String matterDescription;

    @Column(name = "matterRecordingUrl", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String matterRecordingUrl;

    @Column(name = "deadline", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private LocalDate deadline;

    @JsonManagedReference
    @OneToMany(mappedBy = "litigationSupportRequest")
    private List<LitigationSupportRequestFileEntity> files;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "userId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String userId;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "lawyerProfileId", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String lawyerProfileId;

    @OneToOne
    @JoinColumn(name = "lawyerProfileId", insertable = false, updatable = false)
    private LawyerProfileEntity lawyerProfile;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "lawFirmId", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String lawFirmId;

    @OneToOne
    @JoinColumn(name = "lawFirmId", insertable = false, updatable = false)
    private LawFirmEntity lawFirm;

    @Column(name = "paid", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean paid;

    @Column(name = "paymentRequestCode", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String paymentRequestCode;

    @Column(name = "litigationFileNumber", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String litigationFileNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "litigationSupportRequest")
    @OrderBy("time DESC")
    @Builder.Default
    private List<LitigationSupportNegotiationEntity> negotiations = List.of();

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "creatorId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String creatorId;

    @OneToOne
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private UserEntity creator;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;
}
