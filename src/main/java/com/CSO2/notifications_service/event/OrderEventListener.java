package com.CSO2.notifications_service.event;

import com.CSO2.notifications_service.dto.OrderCreatedEvent;
import com.CSO2.notifications_service.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void handleOrderCreated(String message) {
        log.info("Received order-created event: {}", message);
        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);

            // Prepare email context
            Map<String, Object> context = new HashMap<>();
            context.put("orderId", event.getOrderId());
            context.put("items", event.getItems());
            context.put("totalAmount", event.getTotalAmount());

            // Send Email
            notificationService.sendEmail(
                    event.getEmail(),
                    "Order Confirmation - " + event.getOrderId(),
                    "order-confirmation",
                    context,
                    event.getUserId());

            // Create In-App Notification
            notificationService.createInAppNotification(
                    event.getUserId(),
                    "Your order " + event.getOrderId() + " has been placed successfully.",
                    "/orders/" + event.getOrderId());

        } catch (JsonProcessingException e) {
            log.error("Error processing order-created event", e);
        }
    }
}
