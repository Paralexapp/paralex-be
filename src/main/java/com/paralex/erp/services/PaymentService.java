package com.paralex.erp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paralex.erp.commons.utils.Helper;
import com.paralex.erp.documents.PaymentHistoryDocument;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.enums.EventType;
import com.paralex.erp.repositories.PayStackEventRepository;
import com.paralex.erp.repositories.PaymentHistoryRepository;
import com.paralex.erp.repositories.TransactionInitiatorRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class PaymentService {
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final UserEntity userEntity;
    private final Helper helper;
    private final PayStackEventRepository payStackEventRepository;
    private final TransactionInitiatorRepository transactionInitiatorRepository;

    @Value("${paystack.init.transaction.url}")
    private  String transactionInitUrl;

    @Value("${paystack.init.verify-transaction.url}")
    private  String verifyTransactionUrl;

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

    public TransactionResponseDto initTransaction(InitTransactionDto initTransactionDto) throws IOException {
        JSONObject response = helper.makeRequestWithRedirect(helper.writeAsString(initTransactionDto), transactionInitUrl);

        // Create an event for logging or further processing
        PayStackEvent event = new PayStackEvent();
        event.setEvent(Document.parse(helper.writeAsString(response)));
        event.setType(EventType.PAYSTACK_INIT_TRANSACTION_BILL.name());
        payStackEventRepository.save(event);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        TransactionResponseDto transactionResponseDto = mapper.readValue(helper.writeAsString(response), TransactionResponseDto.class);

        TransactionInitiatorData payer = new TransactionInitiatorData();
        payer.setRecipient(transactionResponseDto.getData().getReference());
        payer.setEmail(initTransactionDto.getEmail());
        transactionInitiatorRepository.save(payer);

        return transactionResponseDto;
    }

    public TransactionVerificationDto verifyTransaction(String reference) throws IOException {
        // Prepare the URL for the verification request
        String verifyUrl = String.format("%s/%s", verifyTransactionUrl, reference);

        // Make the GET request to Paystack API
        JSONObject responseJson = helper.makeRequestWithRedirect(null, verifyUrl);

        // Log the event
        PayStackEvent event = new PayStackEvent();
        event.setEvent(Document.parse(helper.writeAsString(responseJson)));
        event.setType(EventType.PAYSTACK_VERIFY_TRANSACTION_BILL.name());
        payStackEventRepository.save(event);

        // Deserialize the response into TransactionVerificationDto
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        TransactionVerificationDto transactionVerificationDto = mapper.readValue(responseJson.toString(), TransactionVerificationDto.class);

        return transactionVerificationDto;
    }
}
