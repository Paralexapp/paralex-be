package com.paralex.erp.repositories;

import com.paralex.erp.dtos.PayStackEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayStackEventRepository extends MongoRepository<PayStackEvent,String> {
}
