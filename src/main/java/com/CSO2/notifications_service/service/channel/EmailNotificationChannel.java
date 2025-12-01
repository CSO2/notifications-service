package com.CSO2.notifications_service.service.channel;

import com.CSO2.notifications_service.dto.NotificationDetails;
import com.CSO2.notifications_service.entity.NotificationLog;
import com.CSO2.notifications_service.repository.NotificationLogRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationChannel implements NotificationChannel {

    private final NotificationLogRepository notificationLogRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void send(NotificationDetails details) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(details.getContext());
        String htmlBody = templateEngine.process(details.getTemplateName(), thymeleafContext);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(details.getRecipientEmail());
            helper.setSubject(details.getSubject());
            helper.setText(htmlBody, true);
            javaMailSender.send(message);

            logNotification(details.getRecipientUserId(), "EMAIL", details.getSubject(), htmlBody, "SENT");
        } catch (Exception e) {
            log.error("Failed to send email to {}", details.getRecipientEmail(), e);
            logNotification(details.getRecipientUserId(), "EMAIL", details.getSubject(), htmlBody, "FAILED");
        }
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.EMAIL;
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
}
