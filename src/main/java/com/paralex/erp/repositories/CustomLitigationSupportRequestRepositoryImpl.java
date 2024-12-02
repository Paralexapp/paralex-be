package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomLitigationSupportRequestRepositoryImpl implements CustomLitigationSupportRequestRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomLitigationSupportRequestRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void paidForBailBond(String paymentRequestCode, boolean paid) {
        Query query = new Query(Criteria.where("paymentRequestCode").is(paymentRequestCode));
        Update update = new Update().set("paid", paid);
        mongoTemplate.updateFirst(query, update, LitigationSupportRequestEntity.class);
    }

    @Override
    public Page<LitigationSupportRequestEntity> findAll(Criteria criteria, PageRequest pageable) {
        Query query = new Query(criteria).with(pageable);

        long totalCount = mongoTemplate.count(query, LitigationSupportRequestEntity.class);
        List<LitigationSupportRequestEntity> results = mongoTemplate.find(query, LitigationSupportRequestEntity.class);

        return new PageImpl<>(results, pageable, totalCount);
    }
}
