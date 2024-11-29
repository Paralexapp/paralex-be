package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(
        name = "litigationSupportNegotiations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "litigationSupportRequestId", "acceptedByClient", "acceptedByLawyer" }) })
@Entity
@DynamicUpdate
@DynamicInsert
public class LitigationSupportNegotiationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "amount", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private int amount;

    @NotNull
    @Column(name = "litigationSupportRequestId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String litigationSupportRequestId;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "litigationSupportRequestId", insertable = false, updatable = false)
    private LitigationSupportRequestEntity litigationSupportRequest;

    @NotNull
    @Column(name = "acceptedByClient", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean acceptedByClient;

    @NotNull
    @Column(name = "acceptedByLawyer", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean acceptedByLawyer;

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
