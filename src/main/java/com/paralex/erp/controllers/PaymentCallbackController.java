package com.paralex.erp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.dtos.TransactionVerificationDto;
import com.paralex.erp.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PaymentCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);

    private final PaymentService paymentService;

    public PaymentCallbackController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(
            @RequestParam(name = "reference", required = false) String verificationDto,
            @RequestParam(name = "status", required = false) String status,
            Model model) {
        // Add attributes to the model to display on the success page
        model.addAttribute("verification", verificationDto);
        model.addAttribute("status", status);
        return "payment-success"; // This points to a Thymeleaf template
    }

    @Operation(summary = "CALLBACK URL", description = "RETURNS TRANSACTION STATUS via JSON response. trxref=transaction reference")
    @GetMapping("/verify-transaction")
    public ResponseEntity<?> verifyTransaction(
            @RequestParam(name = "trxref", required = false) String trxref) {
        try {
            logger.info("Received transaction verification request for reference: {}", trxref);

            // Verify the transaction
            TransactionVerificationDto verificationDto = paymentService.verifyTransaction(trxref);

            logger.info("Transaction verification successful: {}", verificationDto);

            // Build response based on verification result
            if (verificationDto.isSuccessful()) {
                return ResponseEntity.ok(verificationDto); // Return the full DTO for success
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(verificationDto); // Return the DTO for failure
            }
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "JSON processing error occurred."));
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }


}
