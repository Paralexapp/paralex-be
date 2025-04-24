package com.paralex.erp.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDto {
    private String lawyerId;
    private String reviewerId;
    private int rating;
    private String comment;
    private LocalDateTime timestamp; // Optional, but great for frontend display
}
