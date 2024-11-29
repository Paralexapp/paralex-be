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
@Table(name = "lawFirmPracticeAreas")
@Entity
@DynamicUpdate
@DynamicInsert
public class LawFirmPracticeAreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "lawPracticeAreaId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String lawPracticeAreaId;

    @OneToOne
    @JoinColumn(name = "lawPracticeAreaId", insertable = false, updatable = false)
    private LawPracticeAreaEntity lawPracticeArea;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "lawFirmId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String lawFirmId;

    @OneToOne
    @JoinColumn(name = "lawFirmId", insertable = false, updatable = false)
    private LawFirmEntity lawFirm;

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
