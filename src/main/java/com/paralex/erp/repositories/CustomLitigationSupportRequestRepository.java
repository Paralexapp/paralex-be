package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;

public interface CustomLitigationSupportRequestRepository {
    void paidForBailBond(String paymentRequestCode, boolean paid);

    Page<LitigationSupportRequestEntity> findAll(Criteria criteria, PageRequest pageable);
}
