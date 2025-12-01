package com.CSO2.notifications_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDetails {
    private String recipientEmail;
    private String recipientUserId;
    private String subject;
    private String templateName;
    private Map<String, Object> context;
    private String inAppMessage;
    private String inAppLink;
}
