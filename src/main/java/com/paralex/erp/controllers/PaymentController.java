package com.paralex.erp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paralex.erp.documents.PaymentHistoryDocument;
import com.paralex.erp.dtos.*;
import com.paralex.erp.services.BailBondService;
import com.paralex.erp.services.LitigationSupportRequestService;
import com.paralex.erp.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Tag(name = "Payments", description = "APIs to create, GET Payment History, verify transaction")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/payment")
@Log4j2
public class PaymentController {
    private static final String PAYMENT_REQUEST_SUCCESS = "paymentrequest.success";
    private final PaymentService paymentService;
    private final BailBondService bailBondService;
    private final LitigationSupportRequestService litigationSupportRequestService;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    // TODO implement webhook for other payments coming in, if there are...
    // TODO lawyer settlement - use split code
    // TODO rider settlement - use split code
    @Operation(summary = "Paystack's Webhook",
            description = "Should receive payment data from Paystack...")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/webhook")
    public void paymentsWebhook(
            @NotNull @RequestBody PayStackWebhookDataDto paystackWebhookDataDto,
            @NotNull @NotEmpty @NotBlank @RequestHeader("x-paystack-signature header") String paystackSignature) throws JsonProcessingException {
        log.info("[paystack webhook] {}", paystackWebhookDataDto);

        // INFO https://paystack.com/docs/payments/webhooks/#signature-validation
        final var key = paymentService.getSecretKey();
        final var bodyAsString = objectMapper.writeValueAsString(paystackWebhookDataDto);
        //https://www.baeldung.com/java-hmac
        final var mac = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key.getBytes(StandardCharsets.UTF_8))
                .hmacHex(bodyAsString);

        log.info("[paystack webhook] body as string {}", bodyAsString);

        if (!Objects.equals(mac, paystackSignature)) {
            log.info("[paystack webhook] it no match ooo -> mac: {}, signature: {}", mac, paystackSignature);
        }

        // TODO do IP whitelisting -> https://paystack.com/docs/payments/webhooks/#ip-whitelisting
        final var typeOfEvent = paystackWebhookDataDto.getEvent();
        final var webhookData = paystackWebhookDataDto.getData();
        final var status = webhookData.getStatus();
        final var paymentRequestCode = webhookData.getRequestCode();

        // INFO for now, we are only regarding payment request success event
        if (typeOfEvent.equals(PAYMENT_REQUEST_SUCCESS)) {
            if (!Objects.equals(status, "success"))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unbelievable: success event but status is not success");

            bailBondService.findBailBondByRequestCode(paymentRequestCode)
                    .ifPresent(bailBond -> bailBondService.payForBailBond(paymentRequestCode));

            litigationSupportRequestService.findLitigationSupportRequestByRequestCode(paymentRequestCode)
                    .ifPresent((litigationSupportRequest -> litigationSupportRequestService.payForLitigationSupport(paymentRequestCode)));
        }
    }

    @Operation(summary = "Setup User as Customer on Payment Gateway",
            description = "Useful for users that were created before payment gateway was integrated.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create-customer")
    public void setupUserAsPaymentCustomer() throws JsonProcessingException {
        paymentService.setupUserAsPaymentCustomer();
    }

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/verify-transaction")
//    public VerifyTransactionByReferenceResponseDto verifyTransactionByReference(
//            @RequestParam("transactionReference") @NotNull String transactionReference) {
//        return paymentService.verifyTransactionByReference(transactionReference);
//    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/history")
    public List<PaymentHistoryDocument> getPaymentHistory(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return paymentService.getPaymentHistory(paginatedRequestDto);
    }

    @PostMapping("/bill/initialize-payment")
    public ResponseEntity<TransactionResponseDto> initializeTransaction(@RequestBody InitTransactionDto initTransactionDto) {
        try {
            logger.info("Received transaction initialization request: {}", initTransactionDto);

            TransactionResponseDto responseDto = paymentService.initTransaction(initTransactionDto);

            logger.info("Transaction initialization successful: {}", responseDto);

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }

    @PostMapping("bill/verify-transaction")
    public ResponseEntity<TransactionVerificationDto> verifyTransaction(@RequestParam("reference") String reference) {
        try {
            logger.info("Received transaction verification request for reference: {}", reference);

            // Verify the transaction
            TransactionVerificationDto verificationDto = paymentService.verifyTransaction(reference);

            logger.info("Transaction verification successful: {}", verificationDto);

            return new ResponseEntity<>(verificationDto, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
