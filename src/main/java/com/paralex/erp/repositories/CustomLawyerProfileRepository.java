package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface CustomLawyerProfileRepository {
    List<LawyerProfileEntity> searchLawyers(double latitude, double longitude, int limit, int offset);

    void enableProfileByUserId(String userId);

    void disableProfileByUserId(String userId);

    Page<LawyerProfileEntity> findAll(Criteria criteria, PageRequest pageable);
}
