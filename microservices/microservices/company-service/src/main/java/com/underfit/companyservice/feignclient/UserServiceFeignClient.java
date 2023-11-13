package com.underfit.companyservice.feignclient;

import com.underfit.companyservice.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "api-gateway",
        path = "/api/v1/user"
)
public interface UserServiceFeignClient {
    @GetMapping("/exist-user/{userId}")
    ResponseEntity<Boolean> existUserById(@PathVariable("userId") Long userId);

    @GetMapping("/get-user/{userId}")
    ResponseEntity<CompanyDto> getUserById(@PathVariable("userId") Long userId);
}
