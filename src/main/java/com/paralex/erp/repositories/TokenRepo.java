package com.paralex.erp.repositories;

import com.paralex.erp.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepo extends MongoRepository<Token, String> {

    List<Token> findAllByCustomerIdAndIsExpiredAndIsExpired(String customerId, Boolean isExpired, Boolean isExpired2);
}
