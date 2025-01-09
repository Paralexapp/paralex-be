package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerNotification;
import com.paralex.erp.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LawyerNotificationRepository extends MongoRepository<LawyerNotification, String> {
    List<LawyerNotification> findByUserId(String userId);
}
