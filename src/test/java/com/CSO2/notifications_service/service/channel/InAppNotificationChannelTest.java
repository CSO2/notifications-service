package com.CSO2.notifications_service.service.channel;

import com.CSO2.notifications_service.dto.NotificationDetails;
import com.CSO2.notifications_service.entity.UserNotification;
import com.CSO2.notifications_service.repository.UserNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class InAppNotificationChannelTest {

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @InjectMocks
    private InAppNotificationChannel inAppNotificationChannel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void send_ShouldSaveNotification() {
        NotificationDetails details = new NotificationDetails();
        details.setRecipientUserId("user123");
        details.setInAppMessage("Message");
        details.setInAppLink("Link");

        inAppNotificationChannel.send(details);

        verify(userNotificationRepository, times(1)).save(any(UserNotification.class));
    }

    @Test
    void getChannelType_ShouldReturnInApp() {
        assertEquals(ChannelType.IN_APP, inAppNotificationChannel.getChannelType());
    }
}
