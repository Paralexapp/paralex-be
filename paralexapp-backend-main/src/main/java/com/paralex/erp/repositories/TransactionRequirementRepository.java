package com.paralex.erp.repositories;

import com.paralex.erp.entities.TransactionRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRequirementRepository extends JpaRepository<TransactionRequirementEntity, String>, JpaSpecificationExecutor<TransactionRequirementEntity> {
}
