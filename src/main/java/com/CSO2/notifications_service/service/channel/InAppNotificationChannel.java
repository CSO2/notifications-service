package com.CSO2.notifications_service.service.channel;

import com.CSO2.notifications_service.dto.NotificationDetails;
import com.CSO2.notifications_service.entity.UserNotification;
import com.CSO2.notifications_service.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InAppNotificationChannel implements NotificationChannel {

    private final UserNotificationRepository userNotificationRepository;

    @Override
    public void send(NotificationDetails details) {
        UserNotification notification = new UserNotification();
        notification.setUserId(details.getRecipientUserId());
        notification.setMessage(details.getInAppMessage());
        notification.setLink(details.getInAppLink());
        notification.setIsRead(false);
        userNotificationRepository.save(notification);
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.IN_APP;
    }
}
