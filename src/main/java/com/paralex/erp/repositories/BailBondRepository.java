package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public interface BailBondRepository extends MongoRepository<BailBondEntity, String> {

    @Autowired
    MongoTemplate mongoTemplate = null;

    // Approve BailBond Request
    default void approveBailBondRequest(String paymentRequestCode, boolean approved) {
        mongoTemplate.updateFirst(
                new Query().addCriteria(where("paymentRequestCode").is(paymentRequestCode)),
                new Update().set("approved", approved),
                BailBondEntity.class
        );
    }

    // Reject BailBond Request
    default void rejectBailBondRequest(String id, boolean rejected) {
        mongoTemplate.updateFirst(
                new Query().addCriteria(where("id").is(id)),
                new Update().set("rejected", rejected),
                BailBondEntity.class
        );
    }

    // Mark BailBond as paid
    default void paidForBailBond(String paymentRequestCode, boolean paid) {
        mongoTemplate.updateFirst(
                new Query().addCriteria(where("paymentRequestCode").is(paymentRequestCode)),
                new Update().set("paid", paid),
                BailBondEntity.class
        );
    }

    // Withdraw BailBond Request
    default void withdrawBailBondRequest(String id, boolean withdrawn) {
        mongoTemplate.updateFirst(
                new Query().addCriteria(where("id").is(id)),
                new Update().set("withdrawn", withdrawn),
                BailBondEntity.class
        );
    }
}
