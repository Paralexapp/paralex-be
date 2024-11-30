package com.paralex.erp.repositories;

import com.paralex.erp.dtos.SumOfWalletTransactionDto;
import com.paralex.erp.entities.WalletTransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletTransactionRepository extends MongoRepository<WalletTransactionEntity, String> {

    // You can define standard MongoDB queries here if needed

    @Autowired
    MongoTemplate mongoTemplate = null; // Replace with actual MongoTemplate bean injection

    default Optional<SumOfWalletTransactionDto> sumAmountByCreatorIdAndType(String type, String creatorId) {
        // Match operation to filter the documents by 'type' and 'creatorId'
        MatchOperation match = Aggregation.match(
                Criteria.where("type").is(type)
                        .and("creatorId").is(creatorId)
        );

        // Group operation to sum 'amount' based on the conditions
        GroupOperation group = Aggregation.group().sum("amount").as("sum");

        // Perform aggregation query
        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<SumOfWalletTransactionDto> results = mongoTemplate.aggregate(aggregation, WalletTransactionEntity.class, SumOfWalletTransactionDto.class);

        // Return the result as an Optional
        return Optional.ofNullable(results.getUniqueMappedResult());
    }
}
