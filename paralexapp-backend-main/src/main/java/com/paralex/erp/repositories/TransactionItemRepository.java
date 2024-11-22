package com.paralex.erp.repositories;

import com.paralex.erp.entities.TransactionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionItemRepository extends JpaRepository<TransactionItemEntity, String>, JpaSpecificationExecutor<TransactionItemEntity> {
}
