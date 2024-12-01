package com.paralex.erp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.dtos.AddAdjournmentDateDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.SendPaymentLinkDto;
import com.paralex.erp.dtos.SubmitBailBondRequestDto;
import com.paralex.erp.entities.*;
import com.paralex.erp.repositories.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class BailBondService {
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String INVALID_OPERATION_ON_REQUEST_WITH_A_TERMINAL_STATUS = "Invalid Operation on request with a terminal status";
    private final BailBondRepository bailBondRepository;
    private final BailBondOccupationHistoryRepository bailBondOccupationHistoryRepository;
    private final BailBondSpouseDetailRepository bailBondSpouseDetailRepository;
    private final BailBondVehicleDetailRepository bailBondVehicleDetailRepository;
    private final BailBondLandDetailRepository bailBondLandDetailRepository;
    private final BailBondLegalPractitionerRepository bailBondLegalPractitionerRepository;
    private final BailBondNextOfKinDetailRepository bailBondNextOfKinDetailRepository;
    private final BailBondTravelOutsideJurisdictionRepository bailBondTravelOutsideJurisdictionRepository;
    private final BailBondThirdPartyGuarantorRepository bailBondThirdPartyGuarantorRepository;
    private final BailBondAdjournmentDateRepository bailBondAdjournmentDateRepository;
    private final UserEntity userEntity;

    private final PaymentGatewayService paymentGatewayService;
    private final UserService userService;

    public void removeAdjournmentDate(@NotNull String id) {
        bailBondAdjournmentDateRepository.deleteById(id);
    }

    public void addAdjournmentDate(@NotNull AddAdjournmentDateDto addAdjournmentDateDto) {
        bailBondAdjournmentDateRepository.save(BailBondAdjournmentDateEntity.builder()
                        .bailBondId(addAdjournmentDateDto.getBailBondId())
                        .adjournmentDate(addAdjournmentDateDto.getAdjournmentDate())
                        .creatorId(userEntity.getId())
                        .build());
    }

    @Transactional
    public void approveBailBondRequest(@NotNull String id) throws JsonProcessingException {
        final var bailBond = bailBondRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, INVALID_REQUEST));

        if (bailBond.isApproved() || bailBond.isRejected() || bailBond.isWithdrawn())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION_ON_REQUEST_WITH_A_TERMINAL_STATUS);

        final var tenPercentIncrease = bailBond.getTotalAmount() + (bailBond.getTotalAmount() * 0.1);
        final var customerCode = bailBond.getCreator().getCustomerCode();
        final var description = "This is payment for Bail Bond Request you made on Paralex Platform on the day, " + bailBond.getTime().toString();
        final List<SendPaymentLinkDto.LineItemDto> lineItems = List.of((SendPaymentLinkDto.LineItemDto) SendPaymentLinkDto.TaxDto.builder()
                        .name("Total Charge")
                        .amount(bailBond.getTotalAmount())
                .build(),
                (SendPaymentLinkDto.LineItemDto) SendPaymentLinkDto.TaxDto.builder()
                        .name("Service Charge")
                        .amount(bailBond.getTotalAmount() * 0.1)
                        .build());
        final List<SendPaymentLinkDto.TaxDto> taxes = List.of(SendPaymentLinkDto.TaxDto.builder()
                        .name("VAT")
                        .amount(bailBond.getTotalAmount() * 0.75)
                .build());

        // TODO Notify of approval and payment request sent
        // TODO setup controller, admin email notification settings
        final var paymentRequest = paymentGatewayService.sendPaymentLink(SendPaymentLinkDto.builder()
                        .customer(customerCode)
                        .amount(bailBond.getTotalAmount() + tenPercentIncrease)
                        .description(description)
                        .lineItems(lineItems)
                        .tax(taxes)
                .build());

        bailBond.setPaymentRequestCode(paymentRequest.getRequestCode());

        bailBondRepository.approveBailBondRequest(id, true);
        bailBondRepository.rejectBailBondRequest(id, false);
        bailBondRepository.save(bailBond);
    }

    @Transactional
    public void rejectBailBondRequest(@NotNull String id) {
        final var bailBond = bailBondRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, INVALID_REQUEST));

        if (bailBond.isRejected() || bailBond.isApproved() || bailBond.isWithdrawn())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION_ON_REQUEST_WITH_A_TERMINAL_STATUS);

        bailBondRepository.approveBailBondRequest(id, false);
        bailBondRepository.rejectBailBondRequest(id, true);

        // INFO notify of rejection
    }

    @Transactional
    public void withdrawBailBondRequest(@NotNull String id) {
        final var bailBond = bailBondRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, INVALID_REQUEST));

        if (bailBond.isRejected() || bailBond.isApproved() || bailBond.isWithdrawn())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION_ON_REQUEST_WITH_A_TERMINAL_STATUS);

        bailBondRepository.withdrawBailBondRequest(id, true);

        // INFO notify of withdrawal
    }

    public void payForBailBond(@NotNull String requestCode) {
        // INFO Move fund to escrow account
        // INFO save 10% of it when approved
        bailBondRepository.paidForBailBond(requestCode, true);

        // TODO notify customer of payment completion and maybe the lawyer involved or admin
    }

    public List<BailBondEntity> getMyBailBondRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        final var example = Example.of(BailBondEntity.builder()
                        .creatorId(userEntity.getId())
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time"));
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());

        return bailBondRepository.findAll(example, pageable)
                .getContent();
    }

    public List<BailBondEntity> getBailBondRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());

        return bailBondRepository.findAll(pageable)
                .getContent();
    }

    @Transactional
    public void submitBailBondRequest(@NotNull SubmitBailBondRequestDto submitBailBondRequestDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        final double percentCharged = (10 / 100d);
        final var bailBond = bailBondRepository.save(BailBondEntity.builder()
                        .totalAmount(submitBailBondRequestDto.getTotalAmount())
                        .courtId(submitBailBondRequestDto.getCourtId())
                        .investigatingAgency(submitBailBondRequestDto.getInvestigatingAgency())
                        .fullName(submitBailBondRequestDto.getFullName())
                        .nickName(submitBailBondRequestDto.getNickName())
                        .phoneNumber(submitBailBondRequestDto.getPhoneNumber())
                        .workPhoneNumber(submitBailBondRequestDto.getWorkPhoneNumber())
                        .currentHomeAddress(submitBailBondRequestDto.getCurrentHomeAddress())
                        .residenceAddress(submitBailBondRequestDto.getResidenceAddress())
                        .email(submitBailBondRequestDto.getEmail())
                        .durationOfStay(submitBailBondRequestDto.getDurationOfStay())
                        .nameOfLandlord(submitBailBondRequestDto.getDurationOfStay())
                        .nameOfLandlord(submitBailBondRequestDto.getNameOfLandlord())
                        .howLongInCurrentState(submitBailBondRequestDto.getHowLongInCurrentState())
                        .howLongInResidingCity(submitBailBondRequestDto.getHowLongInResidingCity())
                        .formerResidentAddress(submitBailBondRequestDto.getFormerResidentAddress())
                        .dateOfBirth(submitBailBondRequestDto.getDateOfBirth())
                        .placeOfBirth(submitBailBondRequestDto.getPlaceOfBirth())
                        .gender(submitBailBondRequestDto.getGender())
                        .tribe(submitBailBondRequestDto.getTribe())
                        .nationality(submitBailBondRequestDto.getNationality())
                        .nin(submitBailBondRequestDto.getNin())
                        .internationalPassportNumber(submitBailBondRequestDto.getInternationalPassportNumber())
                        .height(submitBailBondRequestDto.getHeight())
                        .weight(submitBailBondRequestDto.getWeight())
                        .eyeColor(submitBailBondRequestDto.getEyeColor())
                        .physicallyChallenged(submitBailBondRequestDto.isPhysicallyChallenged())
                        .memberOfSocialGroup(submitBailBondRequestDto.isMemberOfSocialGroup())
                        .dateOfCurrentArrest(submitBailBondRequestDto.getDateOfCurrentArrest())
                        .arrestingAgency(submitBailBondRequestDto.getArrestingAgency()
                        )
                        .detentionFacilityLocation(submitBailBondRequestDto.getDetentionFacilityLocation())
                .charges(submitBailBondRequestDto.getCharges())
                        .chargeAmount(submitBailBondRequestDto.getChargeAmount())
                        .dateOfLastArrest(submitBailBondRequestDto.getDateOfLastArrest())
                        .lastArrestingAgency(submitBailBondRequestDto.getLastArrestingAgency())
                        .lastArrestCharges(submitBailBondRequestDto.getLastArrestCharges())
                        .existingBailBond(submitBailBondRequestDto.isExistingBailBond())
                        .pendingChargesInJurisdiction(submitBailBondRequestDto.getPendingChargesInJurisdiction())
                        .failedToAppearInCourt(submitBailBondRequestDto.isFailedToAppearInCourt())
                        .enjoyedSuretyBond(submitBailBondRequestDto.isEnjoyedSuretyBond())
                        .detailsOfBond(submitBailBondRequestDto.getDetailsOfBond())
                        .currentEmployerName(submitBailBondRequestDto.getCurrentEmployerName())
                        .durationOfEmployment(submitBailBondRequestDto.getDurationOfEmployment())
                        .position(submitBailBondRequestDto.getPosition())
                        .supervisorName(submitBailBondRequestDto.getSupervisorName())
                        .supervisorWorkPhone(submitBailBondRequestDto.getSupervisorWorkPhone())
                        .formerEmployerName(submitBailBondRequestDto.getFormerEmployerName())
                        .durationOfFormerEmployment(submitBailBondRequestDto.getDurationOfFormerEmployment())
                        .formerPosition(submitBailBondRequestDto.getFormerPosition())
                        .formerSupervisorName(submitBailBondRequestDto.getFormerSupervisorName())
                        .formerSupervisorWorkPhone(submitBailBondRequestDto.getFormerSupervisorWorkPhone())
                        .maritalStatus(submitBailBondRequestDto.getMaritalStatus())
                        .feeCharged((submitBailBondRequestDto.getTotalAmount() * percentCharged) + submitBailBondRequestDto.getTotalAmount())
                        .iAgreeToTermsAndConditions(submitBailBondRequestDto.isIAgreeToTermsAndConditions())
                        .approved(false)
                        .rejected(false)
                        .paid(false)
                        .creatorId(userEntity.getId())
                .build());

        Optional.ofNullable(submitBailBondRequestDto.getOccupations())
                        .ifPresent(items -> bailBondOccupationHistoryRepository.saveAll(items.stream()
                                .map(item -> BailBondOccupationHistoryEntity.builder()
                                        .employerName(item.getEmployerName())
                                        .position(item.getPosition())
                                        .durationInYears(item.getDurationInYears())
                                        .bailBondId(bailBond.getId())
                                        .creatorId(userEntity.getId())
                                        .build())
                                .toList()));

        Optional.ofNullable(submitBailBondRequestDto.getSpouseDetails())
                .ifPresent(item -> bailBondSpouseDetailRepository.save(BailBondSpouseDetailEntity.builder()
                                .name(item.getName())
                                .durationOfMarriage(item.getDurationOfMarriage())
                                .address(item.getAddress())
                                .homePhone(item.getHomePhone())
                                .mobilePhone(item.getMobilePhone())
                                .workPhone(item.getWorkPhone())
                                .nin(item.getNin())
                                .driversLicense(item.getDriversLicense())
                                .internationalPassport(item.getInternationalPassport())
                                .occupation(item.getOccupation())
                                .employer(item.getEmployer())
                                .durationOfEmployment(item.getDurationOfEmployment())
                                .supervisorName(item.getSupervisorName())
                                .bailBondId(bailBond.getId())
                        .creatorId(userEntity.getId())
                        .build()));

        Optional.ofNullable(submitBailBondRequestDto.getVehicleDetails())
                .ifPresent(vehicleDetails -> bailBondVehicleDetailRepository.saveAll(vehicleDetails.stream()
                        .map(item -> BailBondVehicleDetailEntity.builder()
                                .year(item.getYear())
                                .make(item.getMake())
                                .model(item.getModel())
                                .color(item.getColor())
                                .plateNumber(item.getPlateNumber())
                                .state(item.getState())
                                .insuranceCompanyOrAgent(item.getInsuranceCompanyOrAgent())
                                .bailBondId(bailBond.getId())
                                .creatorId(userEntity.getId())
                                .build())
                        .toList()));

        Optional.ofNullable(submitBailBondRequestDto.getLandDetails())
                .ifPresent(landDetails -> bailBondLandDetailRepository.saveAll(landDetails.stream()
                        .map(item -> BailBondLandDetailEntity.builder()
                                .address(item.getAddress())
                                .state(item.getState())
                                .dateOfPurchase(item.getDateOfPurchase())
                                .certificateNumber(item.getCertificateNumber())
                                .bailBondId(bailBond.getId())
                                .creatorId(userEntity.getId())
                                .build())
                        .toList()));

        final var legalPractitioner = submitBailBondRequestDto.getLegalPractitioner();

        bailBondLegalPractitionerRepository.save(BailBondLegalPractitionerEntity.builder()
                        .representativeName(legalPractitioner.getRepresentativeName())
                        .nameOfFirm(legalPractitioner.getNameOfFirm())
                        .address(legalPractitioner.getAddress())
                        .phoneNumber(legalPractitioner.getPhoneNumber())
                        .bailBondId(bailBond.getId())
                .creatorId(userEntity.getId())
                .build());

        final var nextOfKinDetails = submitBailBondRequestDto.getNextOfKinDetails();

        bailBondNextOfKinDetailRepository.save(BailBondNextOfKinDetailEntity.builder()
                        .name(nextOfKinDetails.getName())
                        .relationship(nextOfKinDetails.getRelationship())
                        .homePhone(nextOfKinDetails.getHomePhone())
                        .workPhone(nextOfKinDetails.getWorkPhone())
                        .email(nextOfKinDetails.getEmail())
                        .nationalIdentityNumber(nextOfKinDetails.getNationalIdentityNumber())
                        .driversLicense(nextOfKinDetails.getDriversLicense())
                        .internationalPassport(nextOfKinDetails.getInternationalPassport())
                        .homeAddress(nextOfKinDetails.getHomeAddress())
                        .occupation(nextOfKinDetails.getOccupation())
                        .currentEmployer(nextOfKinDetails.getCurrentEmployer())
                        .currentEmployerAddress(nextOfKinDetails.getCurrentEmployerAddress())
                        .supervisorsName(nextOfKinDetails.getSupervisorsName())
                        .supervisorsPhoneNumber(nextOfKinDetails.getSupervisorsPhoneNumber())
                        .bailBondId(bailBond.getId())
                .creatorId(userEntity.getId())
                .build());

        final var travelOutsideJurisdiction = submitBailBondRequestDto.getTravelOutsideJurisdiction();

        bailBondTravelOutsideJurisdictionRepository.save(BailBondTravelOutsideJurisdictionEntity.builder()
                        .lastTravelOutsideJurisdiction(travelOutsideJurisdiction.getLastTravelOutsideJurisdiction())
                        .durationOfTrip(travelOutsideJurisdiction.getDurationOfTrip())
                        .destination(travelOutsideJurisdiction.getDestination())
                        .nextPlannedTripOutsideJurisdiction(travelOutsideJurisdiction.getNextPlannedTripOutsideJurisdiction())
                        .destinationOfPlannedTrip(travelOutsideJurisdiction.getDestinationOfPlannedTrip())
                        .nextOutOfCountryTrip(travelOutsideJurisdiction.getNextOutOfCountryTrip())
                        .nextOutOfCountryDestination(travelOutsideJurisdiction.getNextOutOfCountryDestination())
                .bailBondId(bailBond.getId())
                .creatorId(userEntity.getId())
                .build());

        final var thirdPartyBondGuarantor = submitBailBondRequestDto.getThirdPartyBondGuarantor();

        bailBondThirdPartyGuarantorRepository.save(BailBondThirdPartyGuarantorEntity.builder()
                        .name(thirdPartyBondGuarantor.getName())
                        .currentAddress(thirdPartyBondGuarantor.getCurrentAddress())
                        .currentEmployerAddress(thirdPartyBondGuarantor.getCurrentEmployerAddress())
                        .nationalIdentityNumber(thirdPartyBondGuarantor.getNationalIdentityNumber())
                        .internationalPassport(thirdPartyBondGuarantor.getInternationalPassport())
                        .driversLicense(thirdPartyBondGuarantor.getDriversLicense())
                        .taxIdentityNumber(thirdPartyBondGuarantor.getTaxIdentityNumber())
                .bailBondId(bailBond.getId())
                .creatorId(userEntity.getId())
                .build());
    }

    public Optional<BailBondEntity> findBailBondByRequestCode(@NotNull String paymentRequestCode) {
        return bailBondRepository.findOne(
                Example.of(BailBondEntity.builder()
                                .paymentRequestCode(paymentRequestCode)
                        .build(),
                ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                        .withIgnorePaths("id", "time")));
    }
}
