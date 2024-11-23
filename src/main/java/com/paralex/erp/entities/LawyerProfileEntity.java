package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lawyerProfiles")
@Entity
@DynamicUpdate
@DynamicInsert
public class LawyerProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "address", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String address;

    @NotNull
    @Column(name = "location", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private Point location;

    @NotNull
    @Column(name = "supremeCourtNumber", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String supremeCourtNumber;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "userId", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String userId;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;

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
