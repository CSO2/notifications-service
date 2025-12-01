package com.CSO2.notifications_service.controller;

import com.CSO2.notifications_service.entity.UserNotification;
import com.CSO2.notifications_service.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserNotifications_ShouldReturnListOfNotifications() {
        String userId = "user123";
        UserNotification notification = new UserNotification();
        notification.setUserId(userId);
        List<UserNotification> notifications = Collections.singletonList(notification);

        when(notificationService.getUserNotifications(userId)).thenReturn(notifications);

        ResponseEntity<List<UserNotification>> response = notificationController.getUserNotifications(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(notifications, response.getBody());
        verify(notificationService, times(1)).getUserNotifications(userId);
    }

    @Test
    void markAsRead_ShouldReturnOk() {
        String notificationId = "notif123";

        ResponseEntity<Void> response = notificationController.markAsRead(notificationId);

        assertEquals(200, response.getStatusCode().value());
        verify(notificationService, times(1)).markAsRead(notificationId);
    }
}
