package com.paralex.erp.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DriverReviewDTO {
    private String driverId;
    private String reviewerId;
    private int rating;
    private String comment;
    private LocalDateTime timestamp; // Optional, but great for frontend display
}
