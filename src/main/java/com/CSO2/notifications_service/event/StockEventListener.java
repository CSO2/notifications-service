package com.CSO2.notifications_service.event;

import com.CSO2.notifications_service.dto.NotificationDetails;
import com.CSO2.notifications_service.dto.event.StockLowEvent;
import com.CSO2.notifications_service.service.NotificationService;
import com.CSO2.notifications_service.service.channel.ChannelType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Value("${admin.email}")
    private String adminEmail;

    @KafkaListener(topics = "stock-low", groupId = "notification-group")
    public void handleStockLow(String message) {
        log.info("Received stock-low event: {}", message);
        try {
            StockLowEvent event = objectMapper.readValue(message, StockLowEvent.class);

            // Prepare email context
            Map<String, Object> context = new HashMap<>();
            context.put("productId", event.getProductId());
            context.put("stockLevel", event.getStockLevel());

            // Build a single notification details object
            NotificationDetails details = NotificationDetails.builder()
                    .recipientEmail(adminEmail)
                    .subject("Low Stock Alert - Product " + event.getProductId())
                    .templateName("stock-low")
                    .context(context)
                    .build();

            // Send Email to Admin
            notificationService.send(details, EnumSet.of(ChannelType.EMAIL));

        } catch (JsonProcessingException e) {
            log.error("Error processing stock-low event", e);
        }
    }
}
