package com.paralex.erp.repositories;

import com.paralex.erp.entities.TransactionRequirementOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRequirementOptionRepository extends JpaRepository<TransactionRequirementOptionEntity, String>, JpaSpecificationExecutor<TransactionRequirementOptionEntity> {
}
