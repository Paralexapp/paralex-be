package com.paralex.erp.repositories;

import com.paralex.erp.entities.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<TransactionEntity, String>{
}
