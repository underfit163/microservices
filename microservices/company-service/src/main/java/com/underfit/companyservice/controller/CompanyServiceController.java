package com.underfit.companyservice.controller;

import com.underfit.companyservice.configuration.Config;
import com.underfit.companyservice.dto.CompanyDto;
import com.underfit.companyservice.service.CompanyService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyServiceController {
    private final Config config;
    private final CompanyService companyService;

    @GetMapping("/get-description")
    public String getServiceDescription() {
        return "Service Name: " + config.getServiceName();
    }

    @GetMapping("/get-companies")
    public ResponseEntity<?> getCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/exist-company/{id}")
    public ResponseEntity<?> existCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.existCompanyById(id));
    }

    @PostMapping("/create-company")
    public ResponseEntity<?> createCompany(@RequestBody CompanyDto companyDto) {
        if (companyDto.getId() != null) {
            companyDto.setId(null);
        }
        return ResponseEntity.ok(companyService.createCompany(companyDto));
    }

    @GetMapping("/get-company/{id}")
    public ResponseEntity<?> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @DeleteMapping("/delete-company/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("delete company: " + id);
    }

    @ExceptionHandler({EntityNotFoundException.class, FeignException.class})
    public ResponseEntity<?> handlerEntityNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
