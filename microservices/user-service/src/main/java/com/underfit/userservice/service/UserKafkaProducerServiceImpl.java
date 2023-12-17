package com.underfit.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserKafkaProducerServiceImpl implements KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${app.kafka.producer.topic}")
    private String topic;

    @Override
    public void send(String key, String message) {
        kafkaTemplate.send(topic, key, message);
        log.info("message sent: {} - {}", key, message);
    }
}
