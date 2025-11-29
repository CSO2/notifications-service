package com.CSO2.notifications_service.service.channel;

import com.CSO2.notifications_service.dto.NotificationDetails;

public interface NotificationChannel {
    void send(NotificationDetails details);
    ChannelType getChannelType();
}
