package com.CSO2.notifications_service.service.channel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class NotificationChannelFactoryTest {

    @Mock
    private NotificationChannel emailChannel;

    @Mock
    private NotificationChannel inAppChannel;

    private NotificationChannelFactory notificationChannelFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emailChannel.getChannelType()).thenReturn(ChannelType.EMAIL);
        when(inAppChannel.getChannelType()).thenReturn(ChannelType.IN_APP);

        List<NotificationChannel> channels = Arrays.asList(emailChannel, inAppChannel);
        notificationChannelFactory = new NotificationChannelFactory(channels);
    }

    @Test
    void getChannel_ShouldReturnCorrectChannel() {
        assertEquals(emailChannel, notificationChannelFactory.getChannel(ChannelType.EMAIL));
        assertEquals(inAppChannel, notificationChannelFactory.getChannel(ChannelType.IN_APP));
    }

    @Test
    void getChannel_ShouldThrowExceptionForUnknownType() {
        // Since we are using an Enum, we can't really pass an "unknown" enum value
        // unless we add one or pass null.
        // But the map key is the enum.
        // If we ask for a channel type that was not in the list passed to constructor,
        // it should fail.

        // However, in the setup we passed both types.
        // If there was another type, say SMS, and we didn't pass a channel for it.

        // Let's assume ChannelType has only EMAIL and IN_APP for now based on previous
        // file reads.
        // If I can't find a type that is not in the map, I can't test the exception
        // easily unless I create the factory with fewer channels.

        List<NotificationChannel> limitedChannels = Arrays.asList(emailChannel);
        NotificationChannelFactory limitedFactory = new NotificationChannelFactory(limitedChannels);

        assertThrows(IllegalArgumentException.class, () -> limitedFactory.getChannel(ChannelType.IN_APP));
    }
}
