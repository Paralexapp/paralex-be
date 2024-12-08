package com.paralex.erp.ledger.repositories;

import com.paralex.erp.ledger.documents.Ledger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends MongoRepository<Ledger,String> {

    @Query("{ 'transactionRef': ?0 }")
    Optional<Ledger> findByTransactionRef(String reference);
    List<Ledger> findAllByWalletIdAndTypeAndCreatedAtBetween(String walletId, String type, LocalDateTime startDate, LocalDateTime endDate);
    List<Ledger> findAllByWalletIdAndCreatedAtBetween(String walletId, LocalDateTime startDate,LocalDateTime endDate);
    List<Ledger> findAllByWalletIdAndType(String walletId, String type);
    List<Ledger> findAllByWalletId(String walletId);

    boolean existsByTransactionRef(String paymentReference);
}
