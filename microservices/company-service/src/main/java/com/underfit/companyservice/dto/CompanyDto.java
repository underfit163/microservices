package com.underfit.companyservice.dto;

import com.underfit.companyservice.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * DTO for {@link com.underfit.companyservice.entity.Company}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CompanyDto implements Serializable {
    private Long id;
    private String name;
    private Long msrn;
    private String description;
    private Long userId;
    private String userName;

    public Company toEntity() {
        Company company = new Company();
        BeanUtils.copyProperties(this, company);
        return company;
    }

    public static CompanyDto toDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        BeanUtils.copyProperties(company, companyDto);
        return companyDto;
    }
}