package com.CSO2.notifications_service.service.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class NotificationChannelFactory {

    private final Map<ChannelType, NotificationChannel> channelMap = new EnumMap<>(ChannelType.class);

    @Autowired
    public NotificationChannelFactory(List<NotificationChannel> channels) {
        for (NotificationChannel channel : channels) {
            channelMap.put(channel.getChannelType(), channel);
        }
    }

    public NotificationChannel getChannel(ChannelType channelType) {
        return Optional.ofNullable(channelMap.get(channelType))
                .orElseThrow(() -> new IllegalArgumentException("Unknown channel type: " + channelType));
    }
}
