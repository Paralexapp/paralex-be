package com.paralex.erp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.dtos.TransactionVerificationDto;
import com.paralex.erp.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("/payment")
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

    @PostMapping("/verify-transaction")
    public String verifyTransaction(
            @RequestParam(name ="reference", required = false) String reference,
            Model model) {
        try {
            logger.info("Received transaction verification request for reference: {}", reference);

            // Verify the transaction
            TransactionVerificationDto verificationDto = paymentService.verifyTransaction(reference);

            logger.info("Transaction verification successful: {}", verificationDto);

            // Add verification data to the model
            model.addAttribute("verification", verificationDto);

            // Determine success or failure based on verification result
            if (verificationDto.isSuccessful()) {
                return "payment-success"; // Success page
            } else {
                return "payment-failure"; // Failure page
            }
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error: ", e);
            model.addAttribute("error", "JSON processing error occurred.");
            return "payment-failure";
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            model.addAttribute("error", "An unexpected error occurred.");
            return "payment-failure";
        }
    }

}
