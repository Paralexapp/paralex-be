package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SubmitBailBondRequestDto {
    @NotNull
    private long totalAmount;

    @NotNull
    @NotEmpty
    @NotBlank
    private String courtId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String investigatingAgency;

    @NotNull
    @NotEmpty
    @NotBlank
    private String fullName;

    @Nullable
    private String nickName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String workPhoneNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currentHomeAddress;

    @NotNull
    @NotEmpty
    @NotBlank
    private String residenceAddress;

    @NotNull
    @NotEmpty
    @NotBlank
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank
    private String durationOfStay;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nameOfLandlord;

    @NotNull
    @NotEmpty
    @NotBlank
    private String howLongInCurrentState;

    @NotNull
    @NotEmpty
    @NotBlank
    private String howLongInResidingCity;

    @NotNull
    @NotEmpty
    @NotBlank
    private String formerResidentAddress;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @NotEmpty
    @NotBlank
    private String placeOfBirth;

    @Pattern(regexp = "male|female")
    @NotNull
    @NotEmpty
    @NotBlank
    private String gender;

    @NotNull
    @NotEmpty
    @NotBlank
    private String tribe;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nationality;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nin;

    @Nullable
    private String internationalPassportNumber;

    @Nullable
    private String height;

    @Nullable
    private String weight;

    @Nullable
    private String eyeColor;

    @NotNull
    private boolean physicallyChallenged;

    @NotNull
    private boolean memberOfSocialGroup;

    @NotNull
    private LocalDate dateOfCurrentArrest;

    @NotNull
    @NotEmpty
    @NotBlank
    private String arrestingAgency;

    @NotNull
    @NotEmpty
    @NotBlank
    private String detentionFacilityLocation;

    @NotNull
    @NotEmpty
    @NotBlank
    private String charges;

    @NotNull
    @NotEmpty
    @NotBlank
    private long chargeAmount;

    @NotNull
    private LocalDate dateOfLastArrest;

    @NotNull
    @NotEmpty
    @NotBlank
    private String lastArrestingAgency;

    @NotNull
    @NotEmpty
    @NotBlank
    private String lastArrestCharges;

    @NotNull
    private boolean existingBailBond;

    @NotNull
    @NotEmpty
    @NotBlank
    private String pendingChargesInJurisdiction;

    @NotNull
    private boolean failedToAppearInCourt;

    @NotNull
    private boolean enjoyedSuretyBond;

    @NotNull
    @NotEmpty
    @NotBlank
    private String detailsOfBond;

    @Nullable
    private List<AddBailBondOccupationDto> occupations;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currentEmployerName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String durationOfEmployment;

    @NotNull
    @NotEmpty
    @NotBlank
    private String position;

    @NotNull
    @NotEmpty
    @NotBlank
    private String supervisorName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String supervisorWorkPhone;

    @NotNull
    @NotEmpty
    @NotBlank
    private String formerEmployerName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String durationOfFormerEmployment;

    @NotNull
    @NotEmpty
    @NotBlank
    private String formerPosition;

    @NotNull
    @NotEmpty
    @NotBlank
    private String formerSupervisorName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String formerSupervisorWorkPhone;

    @Nullable
    private String maritalStatus;

    @Nullable
    private AddBailBondSpouseDetailsDto spouseDetails;

    @Nullable
    private List<AddBailBondVehicleDetailsDto> vehicleDetails;

    @Nullable
    private List<AddBailBondLandDetailsDto> landDetails;

    @NotNull
    private AddLegalPractitionerDto legalPractitioner;

    @NotNull
    private AddNextOfKinDetailsDto nextOfKinDetails;

    @NotNull
    private AddTravelOutsideJurisdictionDto travelOutsideJurisdiction;

    @NotNull
    private AddThirdPartyBondGuarantor thirdPartyBondGuarantor;

    @NotNull
    @NotEmpty
    @NotBlank
    private boolean iAgreeToTermsAndConditions;
}
