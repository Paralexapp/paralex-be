package com.paralex.erp.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverProfileDto {
    private String id;
    private String name;
    private String phoneNumber;
    private String riderPhotoUrl;
    private double distance; // Distance from the delivery destination
}
