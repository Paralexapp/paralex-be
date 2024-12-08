package com.paralex.erp.repositories;

import com.paralex.erp.entities.NewWallet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewWalletRepository extends MongoRepository<NewWallet,String> {
//    List<NewWallet> findAllByEmployeeId(String employeeId);
//    List<NewWallet> findAllByBusinessId(String businessId);
//    Optional<NewWallet> findByBusinessIdAndType(String businessId, String type);
//    List<NewWallet> findByType(String type);
//    Optional<NewWallet> findByBusinessId(String businessId);
//    Optional<NewWallet> findByNewWalletId(String NewWalletId);
//    Optional<NewWallet> findByEmail(String email);
}
