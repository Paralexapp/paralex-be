package com.paralex.erp.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lawyerReviews")
public class LawyerReviewEntity {
    @Id
    private String id;

    @NotNull
    private String lawyerProfileId;

    @NotNull
    private String reviewerId; // ID of the user who gave the review

    @DBRef
    private UserEntity reviewer; // Optional for richer data, like showing reviewer info

    @NotNull
    @Min(1)
    @Max(5)
    private int rating; // Star rating from 1 to 5

    private String comment;

    private LocalDateTime reviewDate;
}
