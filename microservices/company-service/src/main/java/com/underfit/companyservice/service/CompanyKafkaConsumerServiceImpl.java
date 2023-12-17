package com.underfit.companyservice.service;

import com.underfit.companyservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyKafkaConsumerServiceImpl implements KafkaConsumerService {
    private final CompanyRepository companyRepository;
    @Override
    @KafkaListener(topics = "${app.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receive(ConsumerRecord<String, String> record) {
        if (record.key().startsWith("delete")) {
            companyRepository.deleteById(Long.parseLong(record.value()));
            log.info("Company id {} deleted", record.value());
        }
    }
}
