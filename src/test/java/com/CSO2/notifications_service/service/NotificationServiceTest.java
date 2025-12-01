package com.CSO2.notifications_service.service;

import com.CSO2.notifications_service.dto.NotificationDetails;
import com.CSO2.notifications_service.entity.UserNotification;
import com.CSO2.notifications_service.repository.UserNotificationRepository;
import com.CSO2.notifications_service.service.channel.ChannelType;
import com.CSO2.notifications_service.service.channel.NotificationChannel;
import com.CSO2.notifications_service.service.channel.NotificationChannelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Mock
    private NotificationChannelFactory notificationChannelFactory;

    @Mock
    private NotificationChannel notificationChannel;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void send_ShouldSendNotificationViaChannels() {
        NotificationDetails details = new NotificationDetails();
        details.setRecipientUserId("user123");
        Set<ChannelType> channels = Collections.singleton(ChannelType.EMAIL);

        when(notificationChannelFactory.getChannel(ChannelType.EMAIL)).thenReturn(notificationChannel);

        notificationService.send(details, channels);

        verify(notificationChannelFactory, times(1)).getChannel(ChannelType.EMAIL);
        verify(notificationChannel, times(1)).send(details);
    }

    @Test
    void send_ShouldHandleChannelException() {
        NotificationDetails details = new NotificationDetails();
        details.setRecipientUserId("user123");
        Set<ChannelType> channels = Collections.singleton(ChannelType.EMAIL);

        when(notificationChannelFactory.getChannel(ChannelType.EMAIL)).thenReturn(notificationChannel);
        doThrow(new RuntimeException("Channel error")).when(notificationChannel).send(details);

        notificationService.send(details, channels);

        verify(notificationChannelFactory, times(1)).getChannel(ChannelType.EMAIL);
        verify(notificationChannel, times(1)).send(details);
        // Exception should be caught and logged, not thrown
    }

    @Test
    void getUserNotifications_ShouldReturnListOfNotifications() {
        String userId = "user123";
        UserNotification notification = new UserNotification();
        notification.setUserId(userId);
        List<UserNotification> notifications = Collections.singletonList(notification);

        when(userNotificationRepository.findByUserId(userId)).thenReturn(notifications);

        List<UserNotification> result = notificationService.getUserNotifications(userId);

        assertEquals(notifications, result);
        verify(userNotificationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void markAsRead_ShouldUpdateNotificationStatus() {
        String notificationId = "notif123";
        UserNotification notification = new UserNotification();
        notification.setId(notificationId);
        notification.setIsRead(false);

        when(userNotificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(notificationId);

        verify(userNotificationRepository, times(1)).findById(notificationId);
        verify(userNotificationRepository, times(1)).save(notification);
        assertEquals(true, notification.getIsRead());
    }

    @Test
    void markAsRead_ShouldDoNothingIfNotificationNotFound() {
        String notificationId = "notif123";

        when(userNotificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        notificationService.markAsRead(notificationId);

        verify(userNotificationRepository, times(1)).findById(notificationId);
        verify(userNotificationRepository, never()).save(any());
    }
}
