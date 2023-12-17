package com.undrfit.configcloudserver;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name("user-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic companyTopic() {
        return TopicBuilder.name("company-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}