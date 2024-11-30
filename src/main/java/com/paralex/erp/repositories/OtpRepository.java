package com.paralex.erp.repositories;

import com.paralex.erp.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findByOtp(String otp);
    Optional<Otp> findByCustomerIdAndOtp(String customerId, String otp);
}

