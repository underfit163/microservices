package com.underfit.companyservice.service;

import com.underfit.companyservice.dto.CompanyDto;
import com.underfit.companyservice.dto.ResponseCompanyDto;
import com.underfit.companyservice.entity.Company;
import com.underfit.companyservice.feignclient.UserServiceFeignClient;
import com.underfit.companyservice.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final CompanyKafkaProducerServiceImpl companyKafkaProducerService;

    public List<CompanyDto> getAllCompanies() {
        return companyRepository
                .findAll()
                .stream()
                .filter(Company::getEnabled)
                .map(CompanyDto::toDto)
                .peek(companyDto -> {
                    if (companyDto.getUserId() != null)
                        companyDto.setUserName(Objects.requireNonNull(userServiceFeignClient
                                .getUserById(companyDto.getUserId()).getBody()).getUserName());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createCompany(CompanyDto companyDto) {
        if (companyDto.getUserId() != null) {
            Boolean isExist = userServiceFeignClient.existUserById(companyDto.getUserId()).getBody();
            if (Boolean.FALSE.equals(isExist)) {
                throw new EntityNotFoundException(
                        "Пользователя с идентификатором %s не существует".formatted(companyDto.getUserId())
                );
            }
        }
        Company company = companyRepository.save(companyDto.toEntity());
        log.info("Create company: " + companyDto);
        return company.getId();
    }

    public Boolean existCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Компании с идентификатором %s не существует"
                        .formatted(id)));
        if (Boolean.FALSE.equals(company.getEnabled())) {
            throw new EntityNotFoundException(
                    "Компания с идентификатором %s деактивирована".formatted(id));
        }
        return true;
    }

    public ResponseCompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Компании с идентификатором %s не существует"
                        .formatted(id)));
        if (Boolean.FALSE.equals(company.getEnabled())) {
            throw new EntityNotFoundException(
                    "Компания с идентификатором %s деактивирована".formatted(id));
        }
        ResponseCompanyDto responseCompanyDto = new ResponseCompanyDto();
        responseCompanyDto.setCompanyId(company.getId());
        responseCompanyDto.setCompanyName(company.getName());
        return responseCompanyDto;
    }
    @Transactional
    public void deleteCompany(Long id) {
        if(existCompanyById(id)) {
          Company company = companyRepository.findById(id).get();
          company.setEnabled(false);
          companyRepository.save(company);
          companyKafkaProducerService.send("delete" + id, String.valueOf(id));
        }
    }
}