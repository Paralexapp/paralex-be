package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bailBonds")
public class BailBondEntity {

    @Id
    private String id;

    @Field("totalAmount")
    @Setter
    private long totalAmount;

    @Field("feeCharged")
    @Setter
    private double feeCharged;

    @Field("courtId")
    @Setter
    private String courtId;

    @Field("investigatingAgency")
    @Setter
    private String investigatingAgency;

    @Field("fullName")
    @Setter
    private String fullName;

    @Field("nickName")
    @Setter
    private String nickName;

    @Field("phoneNumber")
    @Setter
    private String phoneNumber;

    @Field("workPhoneNumber")
    @Setter
    private String workPhoneNumber;

    @Field("currentHomeAddress")
    @Setter
    private String currentHomeAddress;

    @Field("residenceAddress")
    @Setter
    private String residenceAddress;

    @Field("email")
    @Setter
    private String email;

    @Field("durationOfStay")
    @Setter
    private String durationOfStay;

    @Field("nameOfLandlord")
    @Setter
    private String nameOfLandlord;

    @Field("howLongInCurrentState")
    @Setter
    private String howLongInCurrentState;

    @Field("howLongInResidingCity")
    @Setter
    private String howLongInResidingCity;

    @Field("formerResidentAddress")
    @Setter
    private String formerResidentAddress;

    @Field("dateOfBirth")
    @Setter
    private LocalDate dateOfBirth;

    @Field("placeOfBirth")
    @Setter
    private String placeOfBirth;

    @Field("gender")
    @Setter
    private String gender;

    @Field("tribe")
    @Setter
    private String tribe;

    @Field("nationality")
    @Setter
    private String nationality;

    @Field("nin")
    @Setter
    private String nin;

    @Field("internationalPassportNumber")
    @Setter
    private String internationalPassportNumber;

    @Field("height")
    @Setter
    private String height;

    @Field("weight")
    @Setter
    private String weight;

    @Field("eyeColor")
    @Setter
    private String eyeColor;

    @Field("physicallyChallenged")
    @Setter
    private boolean physicallyChallenged;

    @Field("memberOfSocialGroup")
    @Setter
    private boolean memberOfSocialGroup;

    @Field("dateOfCurrentArrest")
    @Setter
    private LocalDate dateOfCurrentArrest;

    @Field("arrestingAgency")
    @Setter
    private String arrestingAgency;

    @Field("detentionFacilityLocation")
    @Setter
    private String detentionFacilityLocation;

    @Field("chargeAmount")
    @Setter
    private long chargeAmount;

    @Field("dateOfLastArrest")
    @Setter
    private LocalDate dateOfLastArrest;

    @Field("lastArrestingAgency")
    @Setter
    private String lastArrestingAgency;

    @Field("charges")
    @Setter
    private String charges;

    @Field("lastArrestCharges")
    @Setter
    private String lastArrestCharges;

    @Field("existingBailBond")
    @Setter
    private boolean existingBailBond;

    @Field("pendingChargesInJurisdiction")
    @Setter
    private String pendingChargesInJurisdiction;

    @Field("failedToAppearInCourt")
    @Setter
    private boolean failedToAppearInCourt;

    @Field("enjoyedSuretyBond")
    @Setter
    private boolean enjoyedSuretyBond;

    @Field("detailsOfBond")
    @Setter
    private String detailsOfBond;

    @Field("currentEmployerName")
    @Setter
    private String currentEmployerName;

    @Field("durationOfEmployment")
    @Setter
    private String durationOfEmployment;

    @Field("position")
    @Setter
    private String position;

    @Field("supervisorName")
    @Setter
    private String supervisorName;

    @Field("supervisorWorkPhone")
    @Setter
    private String supervisorWorkPhone;

    @Field("formerEmployerName")
    @Setter
    private String formerEmployerName;

    @Field("durationOfFormerEmployment")
    @Setter
    private String durationOfFormerEmployment;

    @Field("formerPosition")
    @Setter
    private String formerPosition;

    @Field("formerSupervisorName")
    @Setter
    private String formerSupervisorName;

    @Field("formerSupervisorWorkPhone")
    @Setter
    @Getter
    private String formerSupervisorWorkPhone;

    @Field("maritalStatus")
    @Setter
    private String maritalStatus;

    @Field("iAgreeToTermsAndConditions")
    @Setter
    private boolean iAgreeToTermsAndConditions;

    @JsonManagedReference
    @DBRef
    private List<BailBondOccupationHistoryEntity> occupationHistories;

    @JsonManagedReference
    @DBRef
    private BailBondSpouseDetailEntity spouseDetails;

    @JsonManagedReference
    @DBRef
    private List<BailBondVehicleDetailEntity> vehicleDetails;

    @JsonManagedReference
    @DBRef
    private List<BailBondLandDetailEntity> landDetail;

    @JsonManagedReference
    @DBRef
    private BailBondLegalPractitionerEntity legalPractitioner;

    @JsonManagedReference
    @DBRef
    private BailBondNextOfKinDetailEntity nextOfKinDetail;

    @JsonManagedReference
    @DBRef
    private BailBondTravelOutsideJurisdictionEntity travelOutsideJurisdiction;

    @JsonManagedReference
    @DBRef
    private BailBondThirdPartyGuarantorEntity thirdPartyGuarantor;

    @JsonManagedReference
    @DBRef
    private List<BailBondAdjournmentDateEntity> adjournmentDates;

    @Field("approved")
    @Setter
    private boolean approved;

    @Field("rejected")
    @Setter
    private boolean rejected;

    @Field("withdrawn")
    @Setter
    private boolean withdrawn;

    @Field("paid")
    @Setter
    private boolean paid;

    @Field("paymentRequestCode")
    @Setter
    private String paymentRequestCode;

    @Field("dateOfPayment")
    private LocalDateTime dateOfPayment;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("creatorId")
    @Setter
    private String creatorId;

    @DBRef
    private UserEntity creator;

    @Field("time")
    private LocalDateTime time;
}
