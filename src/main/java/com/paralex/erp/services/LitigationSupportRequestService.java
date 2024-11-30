package com.paralex.erp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.*;
import com.paralex.erp.repositories.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class LitigationSupportRequestService {
    public static final String CREATOR_ID = "creatorId";
    public static final String INVALID_OPERATION = "Invalid Operation";

    private final PaymentGatewayService paymentGatewayService;
    private final LitigationSupportRequestRepository litigationSupportRequestRepository;
    private final AssignedLitigationSupportRequestRepository assignedLitigationSupportRequestRepository;
    private final UserEntity userEntity;
    private final LitigationSupportRequestFileRepository litigationSupportRequestFileRepository;
    private final LitigationSupportNegotiationRepository litigationSupportNegotiationRepository;
    private final BillOfChargesRepository billOfChargesRepository;

    // INFO no back door, it has to come from paystack
    public void payForLitigationSupport(@NotNull String requestCode) {
        litigationSupportRequestRepository.paidForBailBond(requestCode, true);

        // TODO notify customer of payment completion and maybe the lawyer involved or admin
    }

    public void fillInFileNumber(@NotNull String id, @NotNull SetLitigationSupportFileNumberDto setLitigationSupportFileNumberDto) {
        final var assignedLitigationSupportRequest = assignedLitigationSupportRequestRepository.findById(id)
                .orElseThrow();

        if (!Objects.equals(userEntity.getId(), assignedLitigationSupportRequest.getLawyerProfile().getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION);
        }

        final var litigationSupportRequest = assignedLitigationSupportRequest.getLitigationSupportRequest();

        litigationSupportRequest.setLitigationFileNumber(setLitigationSupportFileNumberDto.getFileNumber());

        litigationSupportRequestRepository.save(litigationSupportRequest);

        // TODO send notification
    }

    public void removeLitigationSupportRequestFile(@NotNull String id) {
        final var litigationSupportRequest = isMyLitigationSupportRequest(userEntity.getId());

        if (litigationSupportRequest.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION);

        litigationSupportRequestRepository.deleteById(id);
    }

    public void addLitigationSupportRequestFile(@NotNull String id, List<AddFileDto> addFileDtoList) {
        litigationSupportRequestFileRepository.saveAll(addFileDtoList.stream()
                .map(item -> LitigationSupportRequestFileEntity.builder()
                        .name(item.getName())
                        .url(item.getUrl())
                        .litigationSupportRequestId(id)
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public void reassignLitigationSupportRequest(@NotNull String id, @NotNull ReAssignLitigationSupportRequestDto reAssignLitigationSupportRequestDto) {
        final var request = findAssignedLitigationSupportRequestById(id).orElseThrow();

        if (!request.isRejected()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Already re-assigned");
        }

        assignedLitigationSupportRequestRepository.save(AssignedLitigationSupportRequestEntity.builder()
                        .lawyerProfileId(reAssignLitigationSupportRequestDto.getLawyerProfileId())
                        .litigationSupportRequestId(id)
                .creatorId(userEntity.getId())
                .build());
    }

    public void rejectLitigationSupportRequest(@NotNull String id) {
        if (isNotAssignedLitigationSupportRequest(id, userEntity.getId()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An error occurred!");

        final var request = findAssignedLitigationSupportRequestById(id)
                .orElseThrow();

        if (!Objects.equals(request.getLawyerProfile().getUserId(), userEntity.getId()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        assignedLitigationSupportRequestRepository.reject(id);
    }

    public void insertBillOfCharges(@NotNull @NotEmpty List<AddBillOfChargeDto> addBillOfChargeList) {
        billOfChargesRepository.saveAll(addBillOfChargeList.stream()
                .map(item -> BillOfChargeEntity.builder()
                        .name(item.getName())
                        .amount(item.getAmount())
                        .litigationSupportRequestId(item.getLitigationSupportRequestId())
                        .build())
                .toList());
    }

    public void acceptNegotiationResolution(@NotNull String id) {
        final var litigationSupportNegotiation = litigationSupportNegotiationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION));
        final var litigationSupportRequest = litigationSupportNegotiation.getLitigationSupportRequest();
        final var userId = userEntity.getId();
        final var clientId = litigationSupportRequest.getUser()
                .getId();
        final var lawyerUserId = litigationSupportRequest.getLawyerProfile()
                .getUser()
                .getId();

        if (litigationSupportNegotiation.isAcceptedByClient() && litigationSupportNegotiation.isAcceptedByLawyer())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction already closed");

        if (!userId.equals(lawyerUserId) && !userId.equals(clientId))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unable to perform transaction");

        if (userId.equals(lawyerUserId))
            litigationSupportNegotiation.setAcceptedByLawyer(true);

        if (userId.equals(clientId))
            litigationSupportNegotiation.setAcceptedByClient(true);

        litigationSupportNegotiationRepository.save(litigationSupportNegotiation);
    }

    public void negotiateCharges(
            @NotNull String id,
            @NotNull NegotiateLitigationSupportRequestDto negotiateLitigationSupportRequestDto) {
        final var litigationSupportRequest = litigationSupportRequestRepository.findById(id)
                .orElseThrow();
        final var creatorId = litigationSupportRequest.getCreatorId();
        final var lawyerProfileId = litigationSupportRequest.getLawyerProfile().getUserId();

        if (creatorId.equals(userEntity.getId()) || lawyerProfileId.equals(userEntity.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, INVALID_OPERATION);
        }

        litigationSupportNegotiationRepository.save(LitigationSupportNegotiationEntity.builder()
                        .amount(negotiateLitigationSupportRequestDto.getProposedAmount())
                        .acceptedByClient(false)
                        .acceptedByLawyer(false)
                        .litigationSupportRequestId(id)
                        .creatorId(userEntity.getId())
                .build());
    }

    public Optional<LitigationSupportNegotiationEntity> findMutuallyAcceptedNegotiation(@NotNull String requestId) {
        return litigationSupportNegotiationRepository.findOne(Example.of(LitigationSupportNegotiationEntity.builder()
                .acceptedByClient(true)
                .acceptedByLawyer(true)
                .litigationSupportRequestId(requestId)
                .build(), ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time")));
    }

    @Transactional
    public void acceptLitigationSupportRequest(
            @NotNull String id,
            @NotNull @NotEmpty List<AddBillOfChargeDto> addBillOfChargeDtoList) throws JsonProcessingException {
        final var acceptedNegotiation = findMutuallyAcceptedNegotiation(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Negotiations have not been mutually accepted by both client and selected lawyer"));

        if (isNotAssignedLitigationSupportRequest(id, userEntity.getId()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "An error occurred!");

        final var assignedLitigationSupportRequestEntity = findAssignedLitigationSupportRequestById(id)
                .orElseThrow();

        if (!Objects.equals(assignedLitigationSupportRequestEntity.getLawyerProfile().getUserId(), userEntity.getId()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_OPERATION);

        // INFO it is assumed that when it is accepted the price is also acceptable, hence send payment link
        // INFO sort out amount to be paid -> total amount and associated bill of charges
        final var customerCode = assignedLitigationSupportRequestEntity.getCreator().getCustomerCode();
        final var description = "This is payment for Litigation Support Request you made on Paralex Platform on the day, " + assignedLitigationSupportRequestEntity.getTime().toString();
        final var amount = acceptedNegotiation.getAmount();

        // INFO validate amount tally with line items
        final var sumOfBillOfCharges = addBillOfChargeDtoList.stream()
                .reduce(0, (cur, item) -> cur + item.getAmount(), Integer::sum);

        if (sumOfBillOfCharges != amount)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid Negotiated amount compared to total of bill of charges");

        // INFO update this with bill of charges
        List<SendPaymentLinkDto.LineItemDto> lineItems = List.of((SendPaymentLinkDto.LineItemDto) SendPaymentLinkDto.TaxDto.builder()
                        .name("Total Charge")
                        .amount(amount)
                        .build());

        lineItems = lineItems.stream()
                .flatMap(lineItemDto -> addBillOfChargeDtoList.stream()
                        .map(item -> (SendPaymentLinkDto.LineItemDto) SendPaymentLinkDto.TaxDto.builder()
                                .name(item.getName())
                                .amount(item.getAmount())
                                .build()))
                .toList();

        final List<SendPaymentLinkDto.TaxDto> taxes = List.of(SendPaymentLinkDto.TaxDto.builder()
                .name("VAT")
                .amount(amount * 0.75)
                .build());

       final var paymentRequest = paymentGatewayService.sendPaymentLink(SendPaymentLinkDto.builder()
                .customer(customerCode)
                .amount(amount)
                .description(description)
                .lineItems(lineItems)
                .tax(taxes)
               .build());

         final var litigationSupportRequest = assignedLitigationSupportRequestEntity.getLitigationSupportRequest();

         litigationSupportRequest.setPaymentRequestCode(paymentRequest.getRequestCode());

        insertBillOfCharges(addBillOfChargeDtoList);

        litigationSupportRequestRepository.save(litigationSupportRequest);
        assignedLitigationSupportRequestRepository.accept(id);
    }

    public boolean isNotAssignedLitigationSupportRequest(
            @NotNull @NotBlank String id,
            @NotNull @NotBlank String userId) {
        return !Objects.equals(assignedLitigationSupportRequestRepository.findById(id)
                .orElseThrow()
                .getLawyerProfile()
                .getUserId(), userId);
    }

    public List<AssignedLitigationSupportRequestEntity> getMyAssignedLitigationSupportRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var example = Example.of(
                AssignedLitigationSupportRequestEntity.builder()
                        .lawyerProfileId(userEntity.getId())
                                .build(),
                ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time", CREATOR_ID));
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());

        return assignedLitigationSupportRequestRepository.findAll(example, pageable)
                .getContent();
    }

    public List<AssignedLitigationSupportRequestEntity> getAssignedLitigationSupportRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var startDate = dateTimePaginatedRequestDto.getStartDate();
        final var endDate = dateTimePaginatedRequestDto.getEndDate();

        final Specification<AssignedLitigationSupportRequestEntity> specification = (root, query, cb) -> cb.between(root.get("time"), startDate, endDate);
        final var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());

        return (List<AssignedLitigationSupportRequestEntity>) assignedLitigationSupportRequestRepository.findAll((Criteria) specification, pageable)
                .getContent();
    }

    public Optional<AssignedLitigationSupportRequestEntity> findAssignedLitigationSupportRequestById(@NotNull String id) {
        return assignedLitigationSupportRequestRepository.findById(id);
    }

    public List<LitigationSupportRequestEntity> getMyLitigationSupportRequest(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var example = Example.of(LitigationSupportRequestEntity.builder()
                        .userId(userEntity.getId())
                .build(), ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time", CREATOR_ID));
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());

        return litigationSupportRequestRepository.findAll(example, pageable)
                .getContent();
    }

    public List<LitigationSupportRequestEntity> getLitigationSupportRequest(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var startDate = dateTimePaginatedRequestDto.getStartDate();
        final var endDate = dateTimePaginatedRequestDto.getEndDate();

        final Specification<LitigationSupportRequestEntity> specification = (root, query, cb) -> cb.between(root.get("time"), startDate, endDate);
        final var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());

        return (List<LitigationSupportRequestEntity>) litigationSupportRequestRepository.findAll((Criteria) specification, pageable)
                .getContent();
    }

    @Transactional
    public void submitLitigationSupportRequest(@NotNull SubmitLitigationSupportRequestDto submitLitigationSupportRequestDto) {
        final var lawyerProfileId = submitLitigationSupportRequestDto.getLawyerProfileId();
        final var lawFirmId = submitLitigationSupportRequestDto.getLawFirmId();
        final var userId = Objects.requireNonNullElse(submitLitigationSupportRequestDto.getUserId(), userEntity.getId());

        Objects.requireNonNullElse(lawyerProfileId, lawFirmId);

        final var litigationSupportRequest = litigationSupportRequestRepository.save(LitigationSupportRequestEntity.builder()
                        .matterTitle(submitLitigationSupportRequestDto.getMatterTitle())
                        .matterDescription(submitLitigationSupportRequestDto.getMatterDescription())
                        .matterRecordingUrl(submitLitigationSupportRequestDto.getMatterRecordingUrl())
                        .deadline(submitLitigationSupportRequestDto.getDeadline())
                        .userId(userId)
                        .lawyerProfileId(lawyerProfileId)
                        .lawFirmId(lawFirmId)
                        .creatorId(userEntity.getId())
                .build());

        litigationSupportRequestFileRepository.saveAll(submitLitigationSupportRequestDto.getFiles()
                .stream()
                .map(item -> LitigationSupportRequestFileEntity.builder()
                        .litigationSupportRequestId(litigationSupportRequest.getId())
                        .name(item.getName())
                        .url(item.getUrl())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());

        litigationSupportNegotiationRepository.save(LitigationSupportNegotiationEntity.builder()
                        .amount(submitLitigationSupportRequestDto.getAmount())
                        .acceptedByClient(false)
                        .acceptedByLawyer(false)
                        .litigationSupportRequestId(litigationSupportRequest.getId())
                .creatorId(userEntity.getId())
                .build());

        assignedLitigationSupportRequestRepository.save(AssignedLitigationSupportRequestEntity.builder()
                        .accepted(false)
                        .rejected(false)
                        .lawyerProfileId(submitLitigationSupportRequestDto.getLawyerProfileId())
                        .litigationSupportRequestId(litigationSupportRequest.getId())
                .creatorId(userEntity.getId())
                .build());
    }

    public Optional<LitigationSupportRequestEntity> isMyLitigationSupportRequest(@NotNull @NotBlank String userId) {
        return litigationSupportRequestRepository.findOne(Example.of(LitigationSupportRequestEntity.builder()
                        .userId(userId)
                .build(), ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time", CREATOR_ID)));
    }

    public Optional<LitigationSupportRequestEntity> findLitigationSupportRequestByRequestCode(@NotNull String requestCode) {
        return litigationSupportRequestRepository.findOne(Example.of(LitigationSupportRequestEntity.builder()
                        .paymentRequestCode(requestCode)
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time")));
    }
}
