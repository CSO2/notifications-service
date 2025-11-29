package com.CSO2.notifications_service.service;

import com.CSO2.notifications_service.dto.NotificationDetails;
import com.CSO2.notifications_service.entity.UserNotification;
import com.CSO2.notifications_service.repository.UserNotificationRepository;
import com.CSO2.notifications_service.service.channel.ChannelType;
import com.CSO2.notifications_service.service.channel.NotificationChannelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final NotificationChannelFactory notificationChannelFactory;

    public void send(NotificationDetails details, Set<ChannelType> channels) {
        log.info("Sending notification for user {} via channels: {}", details.getRecipientUserId(), channels);
        for (ChannelType channelType : channels) {
            try {
                notificationChannelFactory.getChannel(channelType).send(details);
            } catch (Exception e) {
                log.error("Failed to send notification via channel: {}", channelType, e);
            }
        }
    }

    public List<UserNotification> getUserNotifications(String userId) {
        return userNotificationRepository.findByUserId(userId);
    }

    public void markAsRead(String notificationId) {
        userNotificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            userNotificationRepository.save(notification);
        });
    }
}
