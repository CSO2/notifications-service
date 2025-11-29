package com.CSO2.notifications_service.service;

import com.CSO2.notifications_service.entity.NotificationLog;
import com.CSO2.notifications_service.entity.UserNotification;
import com.CSO2.notifications_service.repository.NotificationLogRepository;
import com.CSO2.notifications_service.repository.UserNotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public List<UserNotification> getUserNotifications(String userId) {
        return userNotificationRepository.findByUserId(userId);
    }

    public void markAsRead(String notificationId) {
        userNotificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            userNotificationRepository.save(notification);
        });
    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> context, String userId) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(context);
        String htmlBody = templateEngine.process(templateName, thymeleafContext);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);

            logNotification(userId, "EMAIL", subject, htmlBody, "SENT");
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
            logNotification(userId, "EMAIL", subject, htmlBody, "FAILED");
        }
    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> context) {
        sendEmail(to, subject, templateName, context, null);
    }

    private void logNotification(String userId, String type, String subject, String content, String status) {
        NotificationLog log = new NotificationLog();
        log.setUserId(userId);
        log.setType(type);
        log.setSubject(subject);
        log.setContent(content);
        log.setStatus(status);
        log.setSentAt(LocalDateTime.now());
        notificationLogRepository.save(log);
    }

    public void createInAppNotification(String userId, String message, String link) {
        UserNotification notification = new UserNotification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setIsRead(false);
        userNotificationRepository.save(notification);
    }
}
