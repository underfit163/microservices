package com.underfit.companyservice.service;

public interface KafkaProducerService {
    void send(String key, String message);
}
