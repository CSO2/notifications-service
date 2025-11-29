package com.CSO2.notifications_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification_logs")
public class NotificationLog {
    @Id
    private String id;
    private String userId;
    private String type; // EMAIL, SMS, PUSH
    private String subject;
    private String content;
    private String status; // SENT, FAILED
    private LocalDateTime sentAt;
}
