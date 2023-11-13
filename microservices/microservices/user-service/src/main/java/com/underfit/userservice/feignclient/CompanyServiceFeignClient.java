package com.underfit.userservice.feignclient;

import com.underfit.userservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "api-gateway",
        path = "/api/v1/company"
)
public interface CompanyServiceFeignClient {
    @GetMapping("/exist-company/{companyId}")
    ResponseEntity<Boolean> existCompanyById(@PathVariable("companyId") Long companyId);

    @GetMapping("/get-company/{companyId}")
    ResponseEntity<UserDto> getCompanyById(@PathVariable("companyId") Long companyId);
}
