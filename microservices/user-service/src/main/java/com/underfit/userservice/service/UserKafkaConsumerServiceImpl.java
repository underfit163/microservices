package com.underfit.userservice.service;

import com.underfit.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserKafkaConsumerServiceImpl implements KafkaConsumerService {
    private final UserRepository userRepository;
    private final UserKafkaProducerServiceImpl userKafkaProducerService;

    @Override
    @KafkaListener(topics = "${app.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receive(ConsumerRecord<String, String> record) {
        if (record.key().startsWith("delete")) {
            userRepository.getUsersByCompanyId(Long.parseLong(record.value()))
                    .forEach(user -> {
                        user.setCompanyId(null);
                        userRepository.save(user);
                    });
            userKafkaProducerService.send(record.key(), record.value());
        }
    }
}
