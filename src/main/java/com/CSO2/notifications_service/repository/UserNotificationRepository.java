package com.CSO2.notifications_service.repository;

import com.CSO2.notifications_service.entity.UserNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends MongoRepository<UserNotification, String> {
    List<UserNotification> findByUserId(String userId);
}
