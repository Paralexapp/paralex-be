package com.paralex.erp.repositories;

import com.paralex.erp.dtos.ResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetRequestRepo extends JpaRepository<ResetRequest, String> {
    Optional<ResetRequest> findByResetToken(String resetToken);
}
