package com.paralex.erp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.documents.PaymentHistoryDocument;
import com.paralex.erp.dtos.AddPaymentHistoryDto;
import com.paralex.erp.dtos.CreateCustomerDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.VerifyTransactionByReferenceResponseDto;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.PaymentHistoryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class PaymentService {
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final UserEntity userEntity;

    public String getSecretKey() {
        return paymentGatewayService.getSecretKey();
    }

    public void setupUserAsPaymentCustomer() throws JsonProcessingException {
        paymentGatewayService.createCustomer(CreateCustomerDto.builder()
                        .firstName(userEntity.getName().split("..")[0])
                        .lastName(userEntity.getName().split("..")[1])
                        .email(userEntity.getEmail())
                        .phoneNumber(userEntity.getPhoneNumber())
                .build());
    }

    public VerifyTransactionByReferenceResponseDto verifyTransactionByReference(@NotNull String transactionReference) {
        return paymentGatewayService.verifyTransactionByReference(transactionReference);
    }
    
    public List<PaymentHistoryDocument> getPaymentHistory(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return paymentHistoryRepository.findAll(PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize()))
                .getContent();
    }

    public void addPaymentHistory(@NotNull AddPaymentHistoryDto addPaymentHistoryDto) {
        paymentHistoryRepository.save(PaymentHistoryDocument.builder()
                        .domain(addPaymentHistoryDto.getDomain())
                        .status(addPaymentHistoryDto.getStatus())
                        .transactionId(addPaymentHistoryDto.getTransactionId())
                        .transactionReference(addPaymentHistoryDto.getTransactionReference())
                .receiptNumber(addPaymentHistoryDto.getReceiptNumber())
                        .amount(addPaymentHistoryDto.getAmount())
                        .channel(addPaymentHistoryDto.getChannel())
                        .gatewayResponse(addPaymentHistoryDto.getGatewayResponse())
                        .target(addPaymentHistoryDto.getTarget())
                        .targetId(addPaymentHistoryDto.getTargetId())
                        .creatorId(userEntity.getId())
                .build());
    }
}
