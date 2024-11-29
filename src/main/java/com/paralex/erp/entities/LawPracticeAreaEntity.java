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
@Table(name = "lawPracticeAreas")
@Entity
@DynamicUpdate
@DynamicInsert
public class LawPracticeAreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String name;

    @NotNull
    @Column(name = "description", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String description;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "creatorId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String creatorId;

    @OneToOne
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private UserEntity creator;

    @NotNull
    @Column(name = "status", columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean status;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;
}
