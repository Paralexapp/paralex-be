package com.paralex.erp.repositories;

import com.paralex.erp.entities.TransactionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionItemRepository extends MongoRepository<TransactionItemEntity, String> {
}
