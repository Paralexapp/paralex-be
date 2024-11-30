package com.paralex.erp.repositories;

import com.paralex.erp.dtos.ResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ResetRequestRepo extends MongoRepository<ResetRequest, String> {
    Optional<ResetRequest> findByResetToken(String resetToken);
}
