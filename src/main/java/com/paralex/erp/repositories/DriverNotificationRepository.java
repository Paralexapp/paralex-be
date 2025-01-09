package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverNotification;
import com.paralex.erp.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverNotificationRepository extends MongoRepository<DriverNotification, String> {
    List<DriverNotification> findByUserId(String userId);
}
