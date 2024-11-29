package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bailBonds")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "totalAmount", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private long totalAmount;

    @Column(name = "feeCharged", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private double feeCharged;

    @Column(name = "courtId", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String courtId;

    @Column(name = "investigatingAgency", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String investigatingAgency;

    @Column(name = "fullName", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String fullName;

    @Column(name = "nickName", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String nickName;

    @Column(name = "phoneNumber", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String phoneNumber;

    @Column(name = "workPhoneNumber", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String workPhoneNumber;

    @Column(name = "currentHomeAddress", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String currentHomeAddress;

    @Column(name = "residenceAddress", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String residenceAddress;

    @Column(name = "email", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String email;

    @Column(name = "durationOfStay", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String durationOfStay;

    @Column(name = "nameOfLandlord", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String nameOfLandlord;

    @Column(name = "howLongInCurrentState", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String howLongInCurrentState;

    @Column(name = "howLongInResidingCity", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String howLongInResidingCity;

    @Column(name = "formerResidentAddress", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String formerResidentAddress;

    @Column(name = "dateOfBirth", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private LocalDate dateOfBirth;

    @Column(name = "placeOfBirth", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String placeOfBirth;

    @Column(name = "gender", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String gender;

    @Column(name = "tribe", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String tribe;

    @Column(name = "nationality", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String nationality;

    @Column(name = "nin", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String nin;

    @Column(name = "internationalPassportNumber", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String internationalPassportNumber;

    @Column(name = "height", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String height;

    @Column(name = "weight", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String weight;

    @Column(name = "eyeColor", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String eyeColor;

    @Column(name = "physicallyChallenged", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean physicallyChallenged;

    @Column(name = "memberOfSocialGroup", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean memberOfSocialGroup;

    @Column(name = "dateOfCurrentArrest", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private LocalDate dateOfCurrentArrest;

    @Column(name = "arrestingAgency", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String arrestingAgency;

    @Column(name = "detentionFacilityLocation", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String detentionFacilityLocation;

    @Column(name = "chargeAmount", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private long chargeAmount;

    @Column(name = "dateOfLastArrest", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private LocalDate dateOfLastArrest;

    @Column(name = "lastArrestingAgency", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String lastArrestingAgency;

    @Column(name = "charges", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String charges;

    @Column(name = "lastArrestCharges", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String lastArrestCharges;

    @Column(name = "existingBailBond", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean existingBailBond;

    @Column(name = "pendingChargesInJurisdiction", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String pendingChargesInJurisdiction;

    @Column(name = "failedToAppearInCourt", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean failedToAppearInCourt;

    @Column(name = "enjoyedSuretyBond", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean enjoyedSuretyBond;

    @Column(name = "detailsOfBond", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String detailsOfBond;

    @Column(name = "currentEmployerName", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String currentEmployerName;

    @Column(name = "durationOfEmployment", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String durationOfEmployment;

    @Column(name = "position", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String position;

    @Column(name = "supervisorName", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String supervisorName;

    @Column(name = "supervisorWorkPhone", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String supervisorWorkPhone;

    @Column(name = "formerEmployerName", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String formerEmployerName;

    @Column(name = "durationOfFormerEmployment", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String durationOfFormerEmployment;

    @Column(name = "formerPosition", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String formerPosition;

    @Column(name = "formerSupervisorName", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String formerSupervisorName;

    @Column(name = "formerSupervisorWorkPhone", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String formerSupervisorWorkPhone;

    @Column(name = "maritalStatus", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String maritalStatus;

    @Column(name = "iAgreeToTermsAndConditions", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean iAgreeToTermsAndConditions;

    @JsonManagedReference
    @OneToMany(mappedBy = "bailBond")
    private List<BailBondOccupationHistoryEntity> occupationHistories;

    @JsonManagedReference
    @OneToOne(mappedBy = "bailBond")
    private BailBondSpouseDetailEntity spouseDetails;

    @JsonManagedReference
    @OneToMany(mappedBy = "bailBond")
    private List<BailBondVehicleDetailEntity> vehicleDetails;

    @JsonManagedReference
    @OneToMany(mappedBy = "bailBond")
    private List<BailBondLandDetailEntity> landDetail;

    @JsonManagedReference
    @OneToOne(mappedBy = "bailBond")
    private BailBondLegalPractitionerEntity legalPractitioner;

    @JsonManagedReference
    @OneToOne(mappedBy = "bailBond")
    private BailBondNextOfKinDetailEntity nextOfKinDetail;

    @JsonManagedReference
    @OneToOne(mappedBy = "bailBond")
    private BailBondTravelOutsideJurisdictionEntity travelOutsideJurisdiction;

    @JsonManagedReference
    @OneToOne(mappedBy = "bailBond")
    private BailBondThirdPartyGuarantorEntity thirdPartyGuarantor;

    @JsonManagedReference
    @OneToMany(mappedBy = "bailBond")
    private List<BailBondAdjournmentDateEntity> adjournmentDates;

    @Column(name = "approved", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean approved;

    @Column(name = "rejected", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean rejected;

    @Column(name = "withdrawn", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean withdrawn;

    @Column(name = "paid", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean paid;

    @Column(name = "paymentRequestCode", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String paymentRequestCode;

    @Column(name = "dateOfPayment", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime dateOfPayment;

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
