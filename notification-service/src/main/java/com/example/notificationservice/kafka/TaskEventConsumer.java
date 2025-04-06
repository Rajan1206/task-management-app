package com.example.notificationservice.kafka;

import com.example.notificationservice.model.TaskEvent;
import com.example.notificationservice.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventConsumer {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final String kafkaTopicName = "task-events";

    @KafkaListener(topics = kafkaTopicName, groupId = "notification-group")
    public void consume(ConsumerRecord<String, String> record) throws RuntimeException {
        try {
            TaskEvent event = objectMapper.readValue(record.value(), TaskEvent.class);
            log.info("Received Task Event: {}", event);
            emailService.sendTaskNotification(event);
        } catch (Exception e) {
            log.error("Failed to process task event", e);
            throw new RuntimeException("Failed to process task event", e);
        }
    }
}
