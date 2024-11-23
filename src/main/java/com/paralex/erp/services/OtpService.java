package com.paralex.erp.services;

import com.paralex.erp.commons.utils.EmailContent;
import com.paralex.erp.dtos.EmailDto;
import com.paralex.erp.dtos.GlobalResponse;
import com.paralex.erp.dtos.SendOtpDto;
import com.paralex.erp.entities.Otp;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.exceptions.NotFoundException;
import com.paralex.erp.repositories.OtpRepository;
import com.paralex.erp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final UserRepository customerRepo;
    private final EmailService emailService;
    private final OtpRepository otpRepository;



    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        String otp =  String.format("%04d", random.nextInt(10000));
        Optional<Otp> optional = otpRepository.findById(otp);
        if (optional.isPresent()) {
            generateOtp();
        }
        return otp;
    }


    public GlobalResponse<?> sendOtp(SendOtpDto sendOtpDto){
        UserEntity customer = customerRepo.findByEmail(sendOtpDto.getEmail()).orElseThrow(()-> new NotFoundException("customer not found"));

        String otp_digit = generateOtp();

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        Otp otp = new Otp();
        otp.setCustomerId(customer.getId());
        otp.setEmail(sendOtpDto.getEmail());
        otp.setOtp(otp_digit);
        otpRepository.save(otp);

        String message = EmailContent.verificationEmail(otp_digit);
        EmailDto emailDto = EmailDto.builder()
                .recipient(customer.getEmail())
                .subject("Account Verification")
                .messageBody(message)
                .build();
        emailService.sendOtpEmail(emailDto);

        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("Otp sent successfully");
        return response;
    }
}
