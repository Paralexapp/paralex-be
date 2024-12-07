package com.paralex.erp.repositories;

import com.paralex.erp.dtos.TransactionInitiatorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionInitiatorRepository extends MongoRepository<TransactionInitiatorData,String> {
}
