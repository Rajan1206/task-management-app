package com.example.notificationservice.service.impl;

import com.example.notificationservice.model.TaskEvent;
import com.example.notificationservice.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.sender.email.id}")
    private String senderEmail;

    @Override
    public void sendTaskNotification(TaskEvent event) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(event.getEmailId());
            message.setSubject("Task Notification - " + event.getAction());
            message.setText("Task " + event.getTitle() + " has been " + event.getAction().toLowerCase() + ".");
            mailSender.send(message);
            log.info("Email sent to {}", event.getEmailId());
        } catch (MailException e) {
            log.error("Error sending email to {}: {}", event.getEmailId(), e.getMessage());
        }
    }
}
