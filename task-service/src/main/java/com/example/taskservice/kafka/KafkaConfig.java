package com.example.taskservice.kafka;


import com.example.taskservice.dto.TaskEventDto;
import com.sun.source.util.TaskEvent;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @PostConstruct
    public void init() {
        System.out.println("Kafka bootstrap servers: " + bootstrapServers);
    }
    @Bean
    public ProducerFactory<String, TaskEventDto> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        System.out.println("Kafka bootstrap servers: " + bootstrapServers);
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, TaskEventDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
