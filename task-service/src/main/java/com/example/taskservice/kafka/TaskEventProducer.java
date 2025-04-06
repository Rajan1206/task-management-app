package com.example.taskservice.kafka;

import com.example.taskservice.dto.TaskEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskEventDto> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String kafkaTopicName;

    public void sendTaskEvent(TaskEventDto event) {
        log.info("Sending task event: {}", event);
        kafkaTemplate.send(kafkaTopicName, event);
    }
}