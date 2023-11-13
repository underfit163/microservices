package com.underfit.companyservice.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class Config {
    @Value("${microservice.name}")
    private String serviceName;
}
