package com.paralex.erp.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateDriverProfileDto extends CreateProfileForUserDto {
    @NotNull
    private boolean hasRiderCard;

    @NotNull
    private boolean hasBike;

    @Nullable
    private String bikeType;

    @Nullable
    private String bikeCapacity;

    @Nullable
    private String chassisNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String guarantorClass;

    @NotNull
    @NotEmpty
    @NotBlank
    private String guarantorPhoneNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String guarantorEmail;

    @NotNull
    @NotEmpty
    @NotBlank
    private String guarantorStateOfResidence;

    @NotNull
    @NotEmpty
    @NotBlank
    private String guarantorResidentialAddress;

    @NotNull
    @NotEmpty
    @NotBlank
    private String bvn;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nin;

    @NotNull
    @NotEmpty
    @NotBlank
    private String bankCode;

    @NotNull
    @NotEmpty
    @NotBlank
    private String bankName;

    @Schema(description = "Account Name from Name Enquiry")
    @NotNull
    @NotEmpty
    @NotBlank
    private String accountName;

    @Schema(description = "Driver's location represented by a GeoJSON point")
    private Point location;


    @NotNull
    @NotEmpty
    @NotBlank
    private String accountNumber;

    @Nullable
    private String passportUrl;
}
